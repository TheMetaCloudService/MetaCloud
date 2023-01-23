package eu.themetacloudservice.manager.cloudservices;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.manager.cloudservices.entry.NetworkEntry;
import eu.themetacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.themetacloudservice.manager.cloudservices.entry.TaskedService;
import eu.themetacloudservice.manager.cloudservices.enums.TaskedServiceStatus;
import eu.themetacloudservice.manager.cloudservices.interfaces.ICloudServiceDriver;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.timebaser.TimerBase;
import eu.themetacloudservice.timebaser.utils.TimeUtil;

import java.util.*;
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
        if (!this.entry.group_player_potency.containsKey(entry.getGroupName())){
            this.entry.group_player_potency.put(entry.getGroupName(), 0);
        }
        this.services.add(new TaskedService(entry));
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Service '§f"+entry.getServiceName()+"§r' wurde zur Queue '§fSTART-SERVICES§r' hinzugefügt",
                "the service '"+entry.getServiceName()+"' was added to the queue '§fSTART-SERVICES§r'");
        CloudManager.queueDriver.addQueuedObjectToStart(entry.getServiceName());
        return getService(entry.getServiceName());
    }

    @Override
    public void unregister(String service) {
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Service '§f"+service+"§r' wurde zur Queue '§fSTOP-SERVICES§r' hinzugefügt",
                "the service '"+service+"' was added to the queue '§fSTOP-SERVICES§r'");
        CloudManager.queueDriver.addQueuedObjectToShutdown(service);

    }

    @Override
    public void unregistered(String service) {
        services.removeIf(taskedService -> taskedService.getEntry().getServiceName().equals(service));
    }

    @Override
    public Integer getFreeUUID(String group) {
        Group gs = Driver.getInstance().getGroupDriver().load(group);
        if (gs == null) return 0;
        int maximalTasks = gs.getMaximalOnline() == -1 ? Integer.MAX_VALUE : gs.getMaximalOnline();
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
        return (int) getServices(group).stream().filter(service -> service.getEntry().getStatus() == TaskedServiceStatus.LOBBY || service.getEntry().getStatus() == TaskedServiceStatus.QUEUED || service.getEntry().getStatus() == TaskedServiceStatus.STARTED).count();

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
            }, 40, 40, TimeUtil.SECONDS);

            //packet 2
            new TimerBase().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (CloudManager.shutdown){
                        cancel();
                    }
                    try {
                        Driver.getInstance().getGroupDriver().getAll().parallelStream()
                                .filter(group -> {
                                    int players = getServices(group.getGroup()).stream()
                                            .mapToInt(value -> value.getEntry().getCurrentPlayers()).sum();
                                    return players > 100;
                                })
                                .filter(group -> (group.getOver100AtGroup() * (CloudManager.serviceDriver.entry.group_player_potency.get(group.getGroup()) +1)) < getActiveServices(group.getGroup()))
                                .forEach(group -> {
                                    int newMinimal = (group.getOver100AtGroup() * (CloudManager.serviceDriver.entry.group_player_potency.get(group.getGroup()) +1))- getActiveServices(group.getGroup());
                                    for (int i = 0; i != newMinimal ; i++) {
                                        if (group.getStorage().getRunningNode().equals("InternalNode")){
                                            int memoryAfter = Driver.getInstance().getMessageStorage().canUseMemory- group.getUsedMemory();
                                            if (memoryAfter >= 0){

                                                String id = "";
                                                if (config.getUuid().equals("INT")){
                                                    id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( group.getGroup()));
                                                }else if (config.getUuid().equals("RANDOM")){
                                                    id = getFreeUUID();
                                                }

                                                CloudManager.serviceDriver.register(new TaskedEntry(
                                                        getFreePort(group.getGroupType().equalsIgnoreCase("PROXY")),
                                                        group.getGroup(),
                                                        group.getGroup() + config.getSplitter() + id,
                                                        group.getStorage().getRunningNode(), config.getUseProtocol()));
                                                Driver.getInstance().getMessageStorage().canUseMemory  =Driver.getInstance().getMessageStorage().canUseMemory - group.getUsedMemory();
                                            }else return;
                                        }else {
                                            String id = "";
                                            if (config.getUuid().equals("INT")){
                                                id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( group.getGroup()));
                                            }else if (config.getUuid().equals("RANDOM")){
                                                id = getFreeUUID();
                                            }

                                            CloudManager.serviceDriver.register(new TaskedEntry(
                                                    -1,
                                                    group.getGroup(),
                                                    group.getGroup() + config.getSplitter() + id,
                                                    group.getStorage().getRunningNode(), config.getUseProtocol()));
                                        }
                                    }

                                });

                    }catch (Exception ignored){}
                    if (entry.global_players < 100) return;
                    try {
                        Driver.getInstance().getGroupDriver().getAll()
                                .parallelStream()
                                .filter(group -> NettyDriver.getInstance().nettyServer.isChannelFound(group.getStorage().getRunningNode()) || group.getStorage().getRunningNode().equals("InternalNode"))
                                .filter(group ->  ( group.getOver100AtNetwork() * (CloudManager.serviceDriver.entry.global_player_potency +1)) > getActiveServices(group.getGroup()))
                                .collect(Collectors.toList()).forEach(group -> {
                                    if (group.getStorage().getRunningNode().equals("InternalNode")) {
                                        int freeMemory = Driver.getInstance().getMessageStorage().canUseMemory;
                                        int memoryAfter = freeMemory - group.getUsedMemory();
                                        if (memoryAfter >= 0) {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            Driver.getInstance().getMessageStorage().canUseMemory = memoryAfter;
                                            int port = getFreePort(group.getGroupType().equalsIgnoreCase("PROXY"));
                                            String id = "";
                                            if (config.getUuid().equals("INT")){
                                                id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( group.getGroup()));
                                            }else if (config.getUuid().equals("RANDOM")){
                                                id = getFreeUUID();
                                            }
                                            CloudManager.serviceDriver.register(new TaskedEntry(port, group.getGroup(), group.getGroup() + config.getSplitter() +id, group.getStorage().getRunningNode(), config.getUseProtocol()));

                                        }
                                    }else {
                                        String id = "";
                                        if (config.getUuid().equals("INT")){
                                            id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( group.getGroup()));
                                        }else if (config.getUuid().equals("RANDOM")){
                                            id = getFreeUUID();
                                        }
                                        CloudManager.serviceDriver.register(new TaskedEntry(0, group.getGroup(), group.getGroup() + config.getSplitter() + id, group.getStorage().getRunningNode(), config.getUseProtocol()));
                                    }
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                });


                    }catch (Exception ignored){ }
                    try {
                        getServices().parallelStream()
                                .filter(taskedService -> {
                                    Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());
                                    final double need_players = ((double) group.getMaxPlayers() / (double) 100) * (double) group.getStartNewPercent();

                                    if (taskedService.getEntry().getCurrentPlayers() >= (int) need_players) {
                                        return !taskedService.hasStartedNew;
                                    }else {
                                        return false;
                                    }
                                }).forEach(taskedService -> {
                                    if (taskedService.getEntry().getCheckInterval() == 6) {

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
                                        } else {
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

                                        }

                                    }else {
                                        taskedService.getEntry().setCheckInterval(taskedService.getEntry().getCheckInterval() + 1);
                                    }
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                });


                    }catch (Exception ignored){}
                  try {
                      getServices().parallelStream()
                              .filter(taskedService -> taskedService.getEntry().getCurrentPlayers() == 0 && taskedService.getEntry().getStatus() == TaskedServiceStatus.LOBBY)
                              .forEach(taskedService -> {
                                  if (taskedService.getEntry().getCheckIntervalPlayers() != 12){
                                      taskedService.getEntry().setCheckIntervalPlayers(taskedService.getEntry().getCheckIntervalPlayers()+1);
                                  }else {

                                      Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());
                                      int players = getServices(taskedService.getEntry().getGroupName()).stream()
                                              .mapToInt(value -> value.getEntry().getCurrentPlayers()).sum();

                                      if (CloudManager.serviceDriver.entry.global_players > 100){
                                          int minimal = group.getOver100AtNetwork() * (CloudManager.serviceDriver.entry.global_player_potency +1);

                                          if (minimal < CloudManager.serviceDriver.getActiveServices(group.getGroup())){
                                              CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName());
                                          }
                                      }else if (players > 100){


                                          if ((group.getOver100AtGroup() * (CloudManager.serviceDriver.entry.group_player_potency.get(taskedService.getEntry().getGroupName()) +1)) > CloudManager.serviceDriver.getActiveServices(group.getGroup())){
                                              CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName());
                                          }
                                      }else if (group.getMinimalOnline() > CloudManager.serviceDriver.getActiveServices(group.getGroup())){
                                          CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName());
                                      }

                                      try {
                                          Thread.sleep(200);
                                      } catch (InterruptedException e) {
                                          e.printStackTrace();
                                      }
                                  }
                              });
                  }catch (Exception ignored){}
                }
            }, 5, 2, TimeUtil.MINUTES);


            new TimerBase().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (CloudManager.shutdown){
                        cancel();
                    }
                    try {
                        Driver.getInstance().getGroupDriver().getAll().stream()
                                .filter(group -> getActiveServices(group.getGroup()) < group.getMinimalOnline())
                                .filter(group -> NettyDriver.getInstance().nettyServer.isChannelFound(group.getStorage().getRunningNode()) || group.getStorage().getRunningNode().equals("InternalNode"))
                                .forEach(group -> {
                                    int minimal =  group.getMinimalOnline() - getActiveServices(group.getGroup());
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
            }, 0, 20, TimeUtil.SECONDS);
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
        return this.services.stream().filter(service1 -> service1.getEntry().getServiceName().equals(service)).findFirst().get();
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
