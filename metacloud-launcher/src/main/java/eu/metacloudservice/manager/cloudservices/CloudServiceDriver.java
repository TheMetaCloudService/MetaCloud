package eu.metacloudservice.manager.cloudservices;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.entry.NetworkEntry;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.manager.cloudservices.entry.TaskedService;
import eu.metacloudservice.manager.cloudservices.interfaces.ICloudServiceDriver;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CloudServiceDriver implements ICloudServiceDriver {


    private final ArrayList<TaskedService> services;
    public final NetworkEntry entry;
    private final ManagerConfig config;


    public CloudServiceDriver() {
        entry = new NetworkEntry();
        services = new ArrayList<>();
        handelServices();
        this.config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
    }

    @Override
    public TaskedService register(TaskedEntry entry) {
        if (getService(entry.getServiceName()) != null) {
            return getService(entry.getServiceName());
        }
        if (!this.entry.group_player_potency.containsKey(entry.getGroupName())){
            this.entry.group_player_potency.put(entry.getGroupName(), 0);
        }
        this.services.add(new TaskedService(entry));
        CloudManager.queueDriver.addQueuedObjectToStart(entry.getServiceName());
        return getService(entry.getServiceName());
    }

    @Override
    public void unregister(String service) {
        if (getService(service) == null) return;
        if (NettyDriver.getInstance().nettyServer.isChannelFound(service)) {
            NettyDriver.getInstance().nettyServer.removeChannel(service);
        }

        CloudManager.queueDriver.addQueuedObjectToShutdown(service);
    }

    @Override
    public void unregistered(String service) {
        if (NettyDriver.getInstance().nettyServer.isChannelFound(service)) {
            NettyDriver.getInstance().nettyServer.removeChannel(service);
        }
        services.removeIf(taskedService -> taskedService.getEntry().getServiceName().equals(service));
    }

    @Override
    public Integer getFreeUUID(String group) {
        Group gs = Driver.getInstance().getGroupDriver().load(group);
        if (gs == null) return 0;
        int maximalTasks = gs.getMaximalOnline() == -1 ? Integer.MAX_VALUE : gs.getMaximalOnline() +1;
        return IntStream.range(1, maximalTasks)
                .filter(i -> !getServices(group).stream()
                        .map(s -> Integer.parseInt(s.getEntry().getServiceName().replace(group, "").replace(config.getSplitter(), "")))
                        .collect(Collectors.toList())
                        .contains(i))
                .findFirst()
                .orElse(0);
    }

    @Override
    public String getFreeUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Integer getActiveServices(String group) {
        return (int) getServices(group).stream().filter(service -> service.getEntry().getStatus() == ServiceState.LOBBY || service.getEntry().getStatus() == ServiceState.QUEUED || service.getEntry().getStatus() == ServiceState.STARTED).count();

    }

    @Override
    public Integer getLobbiedServices(String group) {
        return (int) getServices(group).stream().filter(service -> service.getEntry().getStatus() == ServiceState.LOBBY && !service.hasStartedNew).count();
    }

    @Override
    public Integer getFreePort(boolean proxy) {
        List<Integer> ports = getServices().stream()
                .map(TaskedService::getEntry)
                .filter(sEntry -> sEntry.getNode().equals("InternalNode"))
                .map(TaskedEntry::getUsedPort)
                .collect(Collectors.toList());
        return IntStream.range(proxy ? this.config.getBungeecordPort() : this.config.getSpigotPort(), Integer.MAX_VALUE)
                .filter(p -> !ports.contains(p))
                .findFirst()
                .orElse(0);

    }

    @Override
    public void shutdown(ArrayList<String> tasks) {
        this.services.stream().filter(service1 -> tasks.contains(service1.getEntry().getServiceName())).collect(Collectors.toList()).forEach(TaskedService::handelQuit);
    }

    @Override
    public void shutdownNode(String node) {
        this.services.removeIf(service -> service.getEntry().getNode().equals(node));
    }

    @Override
    public void handelServices() {
        Thread current = new Thread(() -> {

            new TimerBase().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (CloudManager.shutdown){
                        cancel();
                    }
                    try {
                        ArrayList<Group> groups = Driver.getInstance().getGroupDriver().getAll();
                        ArrayList<TaskedService> currentServices = getServices();
                        entry.global_players = currentServices.stream()
                                .mapToInt(s -> s.getEntry().getCurrentPlayers())
                                .sum();
                        entry.global_player_potency  = entry.global_players / 100;
                        HashMap<String, Integer> groupPlayerPotency = new HashMap<>();
                        groups.parallelStream()
                                .map(g -> getServices(g.getGroup()))
                                .flatMap(List::stream)
                                .filter(taskedService -> taskedService.getEntry().getCurrentPlayers() > 100)
                                .collect(Collectors.groupingBy(s -> s.getEntry().getGroupName(), Collectors.summingInt(s -> s.getEntry().getCurrentPlayers())))
                                .forEach((key, value) -> groupPlayerPotency.put(key, value / 100));
                        entry.group_player_potency.putAll(groupPlayerPotency);



                    }catch (Exception ignored){

                    }
                }
            }, 20, 20, TimeUtil.SECONDS);

            new TimerBase().schedule(new TimerTask() {
                @Override
                public void run() {

                    List<TaskedService> services = getServices().stream()
                            .filter(taskedService -> taskedService.getEntry().getStatus() == ServiceState.LOBBY)
                            .filter(taskedService -> taskedService.getEntry().getCurrentPlayers() == 0)
                            .filter(taskedService -> {
                                int time = Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - taskedService.getEntry().getTime())));

                                if (time >= 120){
                                    return true;
                                }else {
                                    return false;
                                }

                            })
                            .collect(Collectors.toList());

                    services.stream().sorted(Comparator.comparingInt(o -> Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - o.getEntry().getTime()))))).collect(Collectors.toList()).forEach(taskedService -> {
                        Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());

                        int minonline = 0;

                        if (!entry.group_player_potency.containsKey(group.getGroup())){
                            minonline = group.getMinimalOnline();
                        }else if (entry.group_player_potency.get(group.getGroup()) == 0 && entry.global_player_potency == 0){
                            minonline = group.getMinimalOnline();
                        }else if ( entry.global_player_potency != 0){
                            minonline = group.getOver100AtNetwork()*entry.global_player_potency;
                        }else if (entry.group_player_potency.get(group.getGroup()) != 0){
                            minonline = group.getOver100AtGroup()*entry.group_player_potency.get(group.getGroup());
                        }

                        if (taskedService.hasStartedNew) {
                            unregister(taskedService.getEntry().getServiceName());
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ignored) {
                            }
                        }

                        if (getLobbiedServices(taskedService.getEntry().getGroupName())-1 >=  minonline){
                            unregister(taskedService.getEntry().getServiceName());
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ignored) {

                            }
                        }
                    });


                    List<TaskedService> servicess = getServices().stream()
                            .filter(taskedService -> taskedService.getEntry().getStatus() == ServiceState.LOBBY)
                            .collect(Collectors.toList());


                    servicess.forEach(taskedService -> {

                        Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());
                        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                     /*   PackageConnectedServiceToALL packageConnectedServiceToALL = new PackageConnectedServiceToALL(taskedService.getEntry().getServiceName(),
                                taskedService.getEntry().getGroupName(),
                                taskedService.getEntry().getUsedPort(),
                                taskedService.getEntry().getNode(),
                                config.getNodes().stream().filter(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase(taskedService.getEntry().getNode())).findFirst().get().getAddress(), group.getGroupType(), new ConfigDriver().convert( group));

                        NettyDriver.getInstance().nettyServer.sendToAllSynchronized(packageConnectedServiceToALL);*/
                    });

                    servicess.stream().sorted(Comparator.comparingInt(o -> o.getEntry().getCurrentPlayers())).collect(Collectors.toList()).forEach(taskedService -> {
                        Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());
                        int minonline = 0;

                        if (!entry.group_player_potency.containsKey(group.getGroup())){
                            minonline = group.getMinimalOnline();
                        }else if (entry.group_player_potency.get(group.getGroup()) == 0 && entry.global_player_potency == 0){
                            minonline = group.getMinimalOnline();
                        }else if ( entry.global_player_potency != 0){
                            minonline = group.getOver100AtNetwork()*entry.global_player_potency;
                        }else if (entry.group_player_potency.get(group.getGroup()) != 0){
                            minonline = group.getOver100AtGroup()*entry.group_player_potency.get(group.getGroup());
                        }
                        if (getLobbiedServices(taskedService.getEntry().getGroupName())-1 >=  minonline){
                            unregister(taskedService.getEntry().getServiceName());
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ignored) {

                            }
                        }
                    });



                    servicess.stream().filter(taskedService -> !taskedService.hasStartedNew).collect(Collectors.toList()).stream().filter(taskedService -> {

                        Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());
                        final double need_players = ((double) group.getMaxPlayers() / (double) 100) * (double) group.getStartNewPercent();
                        if (taskedService.getEntry().getCurrentPlayers() >= (int) need_players) {
                            return !taskedService.hasStartedNew;
                        }else {
                            return false;
                        }
                    }).collect(Collectors.toList()).forEach(taskedService -> {

                        taskedService.hasStartedNew = true;
                        if (taskedService.getEntry().getNode().equals("InternalNode")) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String id = "";
                            if (config.getUuid().equals("INT")){
                                id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( taskedService.getEntry().getGroupName()));
                            }else if (config.getUuid().equals("RANDOM")){
                                id = getFreeUUID();
                            }
                            CloudManager.serviceDriver.register(new TaskedEntry(
                                    CloudManager.serviceDriver.getFreePort(Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName()).getGroupType().equalsIgnoreCase("PROXY")),
                                    taskedService.getEntry().getGroupName(),
                                    taskedService.getEntry().getGroupName() + config.getSplitter() + id,
                                    "InternalNode", taskedService.getEntry().isUseProtocol()));
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }else {
                            String id = "";
                            if (config.getUuid().equals("INT")){
                                id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( taskedService.getEntry().getGroupName()));
                            }else if (config.getUuid().equals("RANDOM")){
                                id = getFreeUUID();
                            }
                            CloudManager.serviceDriver.register(new TaskedEntry(
                                    -1,
                                    taskedService.getEntry().getGroupName(),
                                    taskedService.getEntry().getGroupName() + config.getSplitter() + id,
                                    taskedService.getEntry().getNode(), taskedService.getEntry().isUseProtocol()));

                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }, 0, 30, TimeUtil.SECONDS);


            new TimerBase().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (CloudManager.shutdown){
                        cancel();
                    }

                    try {
                        Driver.getInstance().getGroupDriver().getAll().stream()
                                .filter(group -> {
                                    if (!entry.group_player_potency.containsKey(group.getGroup())){
                                        return getActiveServices(group.getGroup()) <  group.getMinimalOnline();
                                    }else if (entry.group_player_potency.get(group.getGroup()) == 0 && entry.global_player_potency == 0){
                                        return getActiveServices(group.getGroup()) < group.getMinimalOnline();
                                    }else if ( entry.global_player_potency != 0){
                                        return getActiveServices(group.getGroup()) < group.getOver100AtNetwork()*entry.global_player_potency;
                                    }else if (entry.group_player_potency.get(group.getGroup()) != 0){
                                        return getActiveServices(group.getGroup()) < group.getOver100AtGroup()*entry.group_player_potency.get(group.getGroup());
                                    }else {
                                        return false;
                                    }

                                })
                                .filter(group -> getServices(group.getGroup()).size() + 1 <= Integer.parseInt(String.valueOf(group.getMaximalOnline()).replace("-1", String.valueOf(Integer.MAX_VALUE))) )
                                .filter(group -> NettyDriver.getInstance().nettyServer.isChannelFound(group.getStorage().getRunningNode()) || group.getStorage().getRunningNode().equals("InternalNode"))
                                .forEach(group -> {

                                    int minonline = 0;
                                    if (!entry.group_player_potency.containsKey(group.getGroup())){
                                        minonline = group.getMinimalOnline();
                                    }else if (entry.group_player_potency.get(group.getGroup()) == 0 && entry.global_player_potency == 0){
                                        minonline = group.getMinimalOnline();
                                    }else if ( entry.global_player_potency != 0){
                                        minonline = group.getOver100AtNetwork()*entry.global_player_potency;
                                    }else if (entry.group_player_potency.get(group.getGroup()) != 0){
                                        minonline = group.getOver100AtGroup()*entry.group_player_potency.get(group.getGroup());
                                    }

                                    int minimal =  minonline - getActiveServices(group.getGroup());
                                    for (int i = 0; i != minimal ; i++) {
                                        if (group.getStorage().getRunningNode().equals("InternalNode")){
                                            int memoryAfter = Driver.getInstance().getMessageStorage().canUseMemory- group.getUsedMemory();
                                            if (memoryAfter >= 0){
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                String id = "";
                                                if (config.getUuid().equals("INT")){
                                                    id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( group.getGroup()));
                                                }else if (config.getUuid().equals("RANDOM")){
                                                    id = getFreeUUID();
                                                }
                                                CloudManager.serviceDriver.register(new TaskedEntry(getFreePort(group.getGroupType().equalsIgnoreCase("PROXY")),
                                                        group.getGroup(), group.getGroup() +
                                                        config.getSplitter() + id,
                                                        group.getStorage().getRunningNode(), config.getUseProtocol()));
                                                Driver.getInstance().getMessageStorage().canUseMemory  = Driver.getInstance().getMessageStorage().canUseMemory -
                                                        group.getUsedMemory();

                                            }
                                        }else {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            String id = "";
                                            if (config.getUuid().equals("INT")){
                                                id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( group.getGroup()));
                                            }else if (config.getUuid().equals("RANDOM")){
                                                id = getFreeUUID();
                                            }
                                            CloudManager.serviceDriver.register(new TaskedEntry(-1, group.getGroup(), group.getGroup() +
                                                    config.getSplitter() +id, group.getStorage().getRunningNode(),
                                                    config.getUseProtocol()));
                                        }

                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                });
                    }catch (Exception ignored){}
                }
            }, 0, 2, TimeUtil.SECONDS);
        });
        current.setPriority(1);
        current.start();
    }

    @Override
    public void updatePlayers(String service, boolean connect) {
        this.services.stream().filter(service1 -> service1.getEntry().getServiceName().equals(service)).findFirst().get().handelCloudPlayerConnection(connect);
    }

    @Override
    public TaskedService getService(String service) {
        if (    this.services.stream().filter(service1 -> service1.getEntry().getServiceName().equals(service)).collect(Collectors.toList()).isEmpty()){
            return null;
        }else {
            return this.services.stream().filter(service1 -> service1.getEntry().getServiceName().equals(service)).findFirst().get();
        }
    }

    @Override
    public ArrayList<TaskedService> getServices() {
        return this.services;
    }

    @Override
    public List<TaskedService> getServices(String group) {
        return this.services.stream().filter(service1 -> service1.getEntry().getGroupName().equals(group)).collect(Collectors.toList());
    }

    @Override
    public List<TaskedService> getServicesFromNode(String node) {
        return this.services.stream().filter(service1 -> service1.getEntry().getNode().equals(node)).collect(Collectors.toList());
    }
}
