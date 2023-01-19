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
import eu.themetacloudservice.manager.cloudservices.interfaces.ICloudTaskDriver;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.timebaser.TimerBase;
import eu.themetacloudservice.timebaser.utils.TimeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class CloudServiceDriver implements ICloudTaskDriver {


    private final ArrayList<TaskedService> services;
    public final NetworkEntry entry;


    public CloudServiceDriver() {
        entry = new NetworkEntry();
        services = new ArrayList<>();
        handelServices();
    }

    @Override
    public TaskedService register(TaskedEntry entry) {

        if (!this.entry.group_player_potency.containsKey(entry.getGroupName())){
            this.entry.group_player_potency.put(entry.getGroupName(), 0);
        }

        this.services.add(new TaskedService(entry));

        return getService(entry.getServiceName());
    }

    @Override
    public void unregister(String service) {
        this.services.stream().filter(service1 -> service1.getEntry().getServiceName().equals(service)).findFirst().get().handelQuit();
        this.services.removeIf(service1 -> service1.getEntry().getServiceName().equals(service));
    }

    @Override
    public Integer getFreeUUID(String group) {
        Group gs = Driver.getInstance().getGroupDriver().load(group);

        ArrayList<Integer> usedID = new ArrayList<>();

        List<TaskedService> services = getServices(group);

        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        services.forEach(taskService -> usedID.add(Integer.parseInt(taskService.getEntry().getGroupName().replace(group, "").replace(config.getSplitter(), ""))));
        int maximalTasks;
        if (gs.getMaximalOnline() == -1){
            maximalTasks = Integer.MAX_VALUE;
        }else {
            maximalTasks = gs.getMaximalOnline();
        }
        for (int i = 1; i !=maximalTasks ; i++) {
            if (!usedID.contains(i)){
                return i;
            }
        }
        return null;
    }

    @Override
    public Integer getActiveServices(String group) {
        return getServices(group).stream().filter(service -> service.getEntry().getStatus() == TaskedServiceStatus.LOBBY || service.getEntry().getStatus() == TaskedServiceStatus.QUEUED)
                .collect(Collectors.toList()).size();

    }

    @Override
    public Integer getFreePort(boolean proxy) {

        ArrayList<Integer> ports = new ArrayList<>();

        List<TaskedService> services = getServices();

        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        services.forEach(taskService -> {
            if (taskService.getEntry().getNode().equals("InternalNode")) {
                ports.add(taskService.getEntry().getUsedPort());
            }
        });
        if (proxy){
            for (int i = config.getBungeecordPort(); i !=Integer.MAX_VALUE ; i++) {
                if (!ports.contains(i)){
                    return i;
                }
            }
        }else {
            for (int i = config.getSpigotPort(); i !=Integer.MAX_VALUE ; i++) {
                if (!ports.contains(i)){
                    return i;
                }
            }
        }

        return null;
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
        TimerBase base = new TimerBase();
        base.schedule(new TimerTask() {
            @Override
            public void run() {
                entry.global_players = 0;
                getServices().forEach(service -> {
                    entry.global_players = entry.global_players+ service.getEntry().getCurrentPlayers();
                });
                entry.global_player_potency = entry.global_players / 100;

                for (int i = 0; i != Driver.getInstance().getGroupDriver().getAll().size() ; i++) {
                    Group group = Driver.getInstance().getGroupDriver().getAll().get(i);
                    int players = 0;
                    List<TaskedService> services = getServices(group.getGroup());
                    for (int j = 0; j != services.size() ; j++) {
                        TaskedService taskedService = services.get(j);
                        players = players + taskedService.getEntry().getCurrentPlayers();
                    }
                    entry.group_player_potency.put(group.getGroup(), players / 100);
                }

            }
        }, 0, 8, TimeUtil.SECONDS);

        TimerBase based = new TimerBase();
        based.schedule(new TimerTask() {
            @Override
            public void run() {


                if (CloudManager.serviceDriver.entry.global_players < 100){
                    Driver.getInstance().getGroupDriver().getAll().forEach(group -> {
                        if (!NettyDriver.getInstance().nettyServer.isChannelFound(group.getStorage().getRunningNode()) && !group.getStorage().getRunningNode().equals("InternalNode")) return;

                        Integer minimal = group.getOver100AtNetwork() * (CloudManager.serviceDriver.entry.global_player_potency +1);
                        if (minimal < getActiveServices(group.getGroup())){
                            int newMinimal = minimal - getActiveServices(group.getGroup());
                            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                            for (int i = 0; i != newMinimal; i++) {
                                if (group.getStorage().getRunningNode().equals("InternalNode")){
                                    TaskedService taskedService = CloudManager.serviceDriver.register(new TaskedEntry(
                                            getFreePort(group.getGroupType().equalsIgnoreCase("PROXY")),
                                            group.getGroup(),
                                            group.getGroup() + config.getSplitter() + CloudManager.serviceDriver.getFreeUUID( group.getGroup()),
                                            group.getStorage().getRunningNode()));

                                    taskedService.handelLaunch();
                                }else {
                                    TaskedService taskedService = CloudManager.serviceDriver.register(new TaskedEntry(
                                            -1,
                                            group.getGroup(),
                                            group.getGroup() + config.getSplitter() + CloudManager.serviceDriver.getFreeUUID( group.getGroup()),
                                            group.getStorage().getRunningNode()));

                                    taskedService.handelLaunch();
                                }

                            }
                        }
                    });

                }else {
                    Driver.getInstance().getGroupDriver().getAll().forEach(group -> {

                        if (!NettyDriver.getInstance().nettyServer.isChannelFound(group.getStorage().getRunningNode()) && !group.getStorage().getRunningNode().equals("InternalNode")) return;
                        int players = 0;
                        List<TaskedService> services = CloudManager.taskDriver.getServices(group.getGroup());
                        for (int j = 0; j != services.size() ; j++) {
                            TaskedService taskedService = services.get(j);
                            players = players + taskedService.getEntry().getCurrentPlayers();
                        }

                        if (players < 100){
                            Integer minimal = group.getOver100AtGroup() * (CloudManager.taskDriver.entry.group_player_potency.get(group.getGroup()) +1);
                            if (minimal < getActiveServices(group.getGroup())){
                                Integer newMinimal = minimal - getActiveServices(group.getGroup());
                                ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                                for (int i = 0; i != newMinimal; i++) {
                                    if (group.getStorage().getRunningNode().equals("InternalNode")){
                                        TaskedService taskedService = CloudManager.taskDriver.register(new TaskedEntry(
                                                getFreePort(group.getGroupType().equalsIgnoreCase("PROXY")),
                                                group.getGroup(),
                                                group.getGroup() + config.getSplitter() + CloudManager.taskDriver.getFreeUUID( group.getGroup()),
                                                group.getStorage().getRunningNode()));

                                        taskedService.handelLaunch();
                                    }else {
                                        TaskedService taskedService = CloudManager.taskDriver.register(new TaskedEntry(
                                                -1,
                                                group.getGroup(),
                                                group.getGroup() + config.getSplitter() + CloudManager.taskDriver.getFreeUUID( group.getGroup()),
                                                group.getStorage().getRunningNode()));

                                        taskedService.handelLaunch();
                                    }

                                }
                            }
                        }

                    });
                }
            }
        }, 10, 25, TimeUtil.SECONDS);
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
