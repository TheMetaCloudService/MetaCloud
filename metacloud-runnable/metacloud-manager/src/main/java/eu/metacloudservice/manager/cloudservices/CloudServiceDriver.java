package eu.metacloudservice.manager.cloudservices;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.listeners.services.CloudProxyLaunchEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceLaunchEvent;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.entry.NetworkEntry;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.manager.cloudservices.entry.TaskedService;
import eu.metacloudservice.manager.cloudservices.interfaces.ICloudServiceDriver;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.out.service.PacketOutServiceConnected;
import eu.metacloudservice.networking.packet.packets.out.service.PacketOutServiceLaunch;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.PacketOutPlayerConnect;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.PlayerGeneral;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServices;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CloudServiceDriver implements ICloudServiceDriver {


    private final ArrayDeque<TaskedService> services;
    public final ArrayDeque<String> delete;
    public final NetworkEntry entry;

    public CloudServiceDriver() {
        entry = new NetworkEntry();
        services = new ArrayDeque<>();
        delete = new ArrayDeque<>();
        handelServices();
    }

    @Override
    public TaskedService register(TaskedEntry entry) {

        if (getService(entry.getServiceName()) != null) {
            if (!entry.getServiceName().endsWith("0")) {
                return getService(entry.getServiceName());
            }
            return null;

        }else if (entry.getServiceName().endsWith("0")){
            return null;
        }else {
            if (!this.entry.group_player_potency.containsKey(entry.getGroupName())) {
                this.entry.group_player_potency.put(entry.getGroupName(), 0);
            }
            TaskedService newService = new TaskedService(entry);

            services.add(newService);
            CloudManager.queueDriver.addQueuedObjectToStart(entry.getServiceName());

            return newService;
        }

    }

    @Override
    public void unregister(String service) {
        if (getService(service) == null) return;
        if (NettyDriver.getInstance().nettyServer.isChannelFound(service))   NettyDriver.getInstance().nettyServer.removeChannel(service);
        CloudManager.serviceDriver.getService(service).getEntry().setStatus(ServiceState.QUEUED);
        CloudManager.queueDriver.addQueuedObjectToShutdown(service);
    }

    @Override
    public void unregistered(String service) {
        if (NettyDriver.getInstance().nettyServer.isChannelFound(service))  NettyDriver.getInstance().nettyServer.removeChannel(service);
        int memory = Driver.getInstance().getGroupDriver().load(getService(service).getEntry().getGroupName()).getUsedMemory();
        Driver.getInstance().getMessageStorage().canUseMemory = Driver.getInstance().getGroupDriver().load(getService(service).getEntry().getGroupName()).getUsedMemory() + memory ;
        services.removeIf(taskedService -> taskedService.getEntry().getServiceName().equals(service));
    }

    @Override
    public Integer getFreeUUID(String group) {
        return Optional.ofNullable(Driver.getInstance().getGroupDriver().load(group)).map(gs -> IntStream.range(1, gs.getMaximalOnline() == -1 ? Integer.MAX_VALUE : gs.getMaximalOnline() + 1).filter(i -> !getServices(group).stream().map(s -> Integer.parseInt(s.getEntry().getServiceName().replace(group, "").replace(CloudManager.config.getSplitter(), ""))).toList().contains(i)).findFirst().orElse(0)).orElse(0);
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
        return IntStream.range(proxy ? CloudManager.config.getBungeecordPort() : CloudManager.config.getSpigotPort(), Integer.MAX_VALUE).filter(p -> !getServices().stream().map(TaskedService::getEntry).filter(sEntry -> sEntry.getNode().equals("InternalNode")).map(TaskedEntry::getUsedPort).toList().contains(p)).findFirst().orElse(0);
    }

    @Override
    public void shutdown(ArrayList<String> tasks) {
        this.services.stream().filter(service1 -> tasks.contains(service1.getEntry().getServiceName())).toList().forEach(TaskedService::handelQuit);
        tasks.forEach(this::unregistered);
    }

    @Override
    public void shutdownNode(String node) {
        this.services.removeIf(service -> service.getEntry().getNode().equals(node));
    }

    @Override
    public void handelServices() {
        Thread current = new Thread(() -> {


            /*
            *
            * Queue to handle start and shutdowns
            *
            * */

            new TimerBase().scheduleAsync(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (Driver.getInstance().getMessageStorage().getCPULoad() <= CloudManager.config.getProcessorUsage()) {
                            int startedCount = getServices().stream()
                                    .filter(ts -> ts.getEntry().getStatus() == ServiceState.STARTED).toList().size();
                            if (startedCount <= CloudManager.config.getServiceStartupCount() && !CloudManager.queueDriver.getQueue_startup().isEmpty()) {
                                String service = CloudManager.queueDriver.getQueue_startup().removeFirst();
                                String groupName = CloudManager.serviceDriver.getService(service).getEntry().getGroupName();
                                String node = CloudManager.serviceDriver.getService(service).getEntry().getNode();
                                Driver.getInstance().getMessageStorage().eventDriver.executeEvent(Driver.getInstance().getGroupDriver().load(groupName).getGroupType().equalsIgnoreCase("PROXY") ? new CloudProxyLaunchEvent(service, groupName, node) : new CloudServiceLaunchEvent(service, groupName, node));
                                NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutServiceLaunch(service, Driver.getInstance().getGroupDriver().load(groupName).getGroupType().equalsIgnoreCase("PROXY"), groupName, node));
                                CloudManager.serviceDriver.getService(service).handelStatusChange(ServiceState.STARTED);
                                CloudManager.serviceDriver.getService(service).handelLaunch();
                            }
                            if (!CloudManager.queueDriver.getQueue_shutdown().isEmpty()) {
                                String service = CloudManager.queueDriver.getQueue_shutdown().removeFirst();
                                CloudManager.serviceDriver.getService(service).handelQuit();
                                CloudManager.serviceDriver.unregistered(service);
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }, 0, 100, TimeUtil.MILLISECONDS);


            /*
            *
            * Update the minimal amount of services if 100 players on Group
            * it also checks if 100 players on the howl network
            *
            * */

            new TimerBase().scheduleAsync(new TimerTask() {
                @Override
                public void run() {
                    if (CloudManager.shutdown){
                        cancel();
                    }
                    ArrayList<Group> groups = Driver.getInstance().getGroupDriver().getAll();
                    ArrayDeque<TaskedService> currentServices = getServices();
                    entry.global_players = currentServices.stream()
                            .mapToInt(s -> s.getEntry().getCurrentPlayers())
                            .sum();
                    entry.global_player_potency = entry.global_players / 100;
                    HashMap<String, Integer> groupPlayerPotency = groups.parallelStream()
                            .map(g -> getServices(g.getGroup()))
                            .flatMap(List::stream)
                            .filter(taskedService -> taskedService.getEntry().getCurrentPlayers() > 100)
                            .collect(
                                    Collectors.groupingBy(s -> s.getEntry().getGroupName(), Collectors.summingInt(s -> s.getEntry().getCurrentPlayers()))
                            )
                            .entrySet()
                            .stream()
                            .collect(
                                    HashMap::new,
                                    (map, entry) -> map.put(entry.getKey(), entry.getValue() / 100),
                                    HashMap::putAll
                            );
                    entry.group_player_potency.putAll(groupPlayerPotency);


                }
            }, 5, 5, TimeUtil.SECONDS);


            /*
            * filters TaskedService objects based on status, player count, and time, removes services,
            * inserts delays, and potentially starts new services, depending on various conditions.
            * */
            new TimerBase().scheduleAsync(new TimerTask() {
                @Override
                public void run() {

                    if (!CloudManager.shutdown) {
                        List<TaskedService> services = getServices().stream()
                                .filter(taskedService -> taskedService.getEntry().getStatus() == ServiceState.LOBBY)
                                .filter(taskedService -> taskedService.getEntry().getCurrentPlayers() == 0)
                                .filter(taskedService -> Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - taskedService.getEntry().getTime()))) >= 120)
                                        .toList();

                        services.forEach(taskedService -> {
                            Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());
                            int online = (!entry.group_player_potency.containsKey(group.getGroup())) ? group.getMinimalOnline()
                                    : (entry.group_player_potency.get(group.getGroup()) == 0 && entry.global_player_potency == 0) ? group.getMinimalOnline()
                                    : (entry.global_player_potency != 0) ? group.getOver100AtNetwork() * entry.global_player_potency
                                    : group.getOver100AtGroup() * entry.group_player_potency.get(group.getGroup());

                            if (taskedService.hasStartedNew) {
                                unregister(taskedService.getEntry().getServiceName());
                            }

                            if (getLobbiedServices(taskedService.getEntry().getGroupName()) - 1 >= online) {
                                unregister(taskedService.getEntry().getServiceName());
                            }
                        });

                        List<TaskedService> servicess = getServices().stream()
                                .filter(taskedService -> taskedService.getEntry().getStatus() == ServiceState.LOBBY)
                                .toList();

                        servicess.forEach(taskedService -> {
                            Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());
                            int minonline = (!entry.group_player_potency.containsKey(group.getGroup())) ? group.getMinimalOnline()
                                    : (entry.group_player_potency.get(group.getGroup()) == 0 && entry.global_player_potency == 0) ? group.getMinimalOnline()
                                    : (entry.global_player_potency != 0) ? group.getOver100AtNetwork() * entry.global_player_potency
                                    : group.getOver100AtGroup() * entry.group_player_potency.get(group.getGroup());

                            int inStoppedQueue = CloudManager.queueDriver.getQueue_shutdown().stream()
                                    .filter(s -> getService(s).getEntry().getGroupName().equalsIgnoreCase(taskedService.getEntry().getGroupName()))
                                    .toList().size();

                            if (getLobbiedServices(taskedService.getEntry().getGroupName()) - 1 - inStoppedQueue >= minonline) {
                                unregister(taskedService.getEntry().getServiceName());
                            }
                        });

                        servicess.stream()
                                .filter(taskedService -> !taskedService.hasStartedNew)
                                .filter(taskedService -> {
                                    Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());
                                    final double need_players = ((double) group.getMaxPlayers() / (double) 100) * (double) group.getStartNewPercent();
                                    return taskedService.getEntry().getCurrentPlayers() >= (int) need_players && !taskedService.hasStartedNew;
                                }).forEach(taskedService -> {
                                    taskedService.hasStartedNew = true;
                                    String id = CloudManager.config.getUuid().equals("INT") ? String.valueOf(CloudManager.serviceDriver.getFreeUUID(taskedService.getEntry().getGroupName())) : getFreeUUID();
                                    if (taskedService.getEntry().getNode().equals("InternalNode")) {
                                        CloudManager.serviceDriver.register(new TaskedEntry(
                                                CloudManager.serviceDriver.getFreePort(Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName()).getGroupType().equalsIgnoreCase("PROXY")),
                                                taskedService.getEntry().getGroupName(),
                                                taskedService.getEntry().getGroupName() + CloudManager.config.getSplitter() + id,
                                                "InternalNode", taskedService.getEntry().isUseProtocol(), id));
                                    } else {
                                        CloudManager.serviceDriver.register(new TaskedEntry(
                                                -1,
                                                taskedService.getEntry().getGroupName(),
                                                taskedService.getEntry().getGroupName() + CloudManager.config.getSplitter() + id,
                                                taskedService.getEntry().getNode(), taskedService.getEntry().isUseProtocol(), id));
                                    }
                                });
                    }
                }
            }, 0, 30, TimeUtil.SECONDS);


            /*
            *
            * A timer periodically checks and starts services based on specified criteria,
            * while handling shutdown events.
            *
            * */
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (CloudManager.shutdown){
                        cancel();
                    }
                    Driver.getInstance().getGroupDriver().getAll().stream()
                            .filter(group -> (!entry.group_player_potency.containsKey(group.getGroup())) ? (getActiveServices(group.getGroup()) < group.getMinimalOnline()) : ((entry.group_player_potency.get(group.getGroup()) == 0 && entry.global_player_potency == 0) ? (getActiveServices(group.getGroup()) < group.getMinimalOnline()) : ((entry.global_player_potency != 0) ? (getActiveServices(group.getGroup()) < group.getOver100AtNetwork() * entry.global_player_potency) : (getActiveServices(group.getGroup()) < group.getOver100AtGroup() * entry.group_player_potency.get(group.getGroup())))))
                            .filter(group -> getServices(group.getGroup()).size() + 1 <= Integer.parseInt(String.valueOf(group.getMaximalOnline()).replace("-1", String.valueOf(Integer.MAX_VALUE))) )
                            .filter(group -> NettyDriver.getInstance().nettyServer.isChannelFound(group.getStorage().getRunningNode()) || group.getStorage().getRunningNode().equals("InternalNode"))
                            .sorted(Comparator.comparingInt(Group::getStartPriority).reversed())
                            .forEach(group -> {
                                int online = (!entry.group_player_potency.containsKey(group.getGroup())) ? group.getMinimalOnline() : (entry.group_player_potency.get(group.getGroup()) == 0 && entry.global_player_potency == 0) ? group.getMinimalOnline() : (entry.global_player_potency != 0) ? group.getOver100AtNetwork() * entry.global_player_potency : group.getOver100AtGroup() * entry.group_player_potency.get(group.getGroup());
                                if (!delete.contains(group.getGroup())) {
                                    int minimal = online - getActiveServices(group.getGroup());
                                    for (int i = 0; i != minimal; i++) {
                                        String id = CloudManager.config.getUuid().equals("INT") ? String.valueOf(CloudManager.serviceDriver.getFreeUUID(group.getGroup())) : getFreeUUID();
                                        String entryName = group.getGroup() + CloudManager.config.getSplitter() + id;
                                        String node = group.getStorage().getRunningNode();
                                        int freePort = getFreePort(group.getGroupType().equalsIgnoreCase("PROXY"));
                                        int memoryAfter = Driver.getInstance().getMessageStorage().canUseMemory - group.getUsedMemory();

                                        if (node.equals("InternalNode") && memoryAfter >= 0) {
                                            CloudManager.serviceDriver.register(new TaskedEntry(freePort, group.getGroup(), entryName, node, CloudManager.config.getUseProtocol(), id));
                                            Driver.getInstance().getMessageStorage().canUseMemory = memoryAfter;
                                        } else if (!node.equals("InternalNode")) {
                                            CloudManager.serviceDriver.register(new TaskedEntry(-1, group.getGroup(), entryName, node, CloudManager.config.getUseProtocol(), id));
                                        }
                                    }
                                }
                            });
                }
            }, 0, 1000);


            /*
            *
            * checks if a service has crashed
            *
             * */
            new TimerBase().scheduleAsync(new TimerTask() {
                @Override
                public void run() {
                    if (!CloudManager.shutdown) {
                        CloudManager.serviceDriver.getServices().parallelStream().filter(taskedService -> taskedService.getEntry().getStatus() != ServiceState.QUEUED || taskedService.getEntry().getStatus() != ServiceState.STARTED).forEach(taskedService -> {
                            if (Driver.getInstance().getWebServer().isContentExists(Driver.getInstance().getWebServer().getRoute("/cloudservice/" + taskedService.getEntry().getServiceName().replace(CloudManager.config.getSplitter(), "~")))){
                                LiveServices ls = (LiveServices) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudservice/" + taskedService.getEntry().getServiceName().replace(CloudManager.config.getSplitter(), "~")), LiveServices.class);
                                if (ls != null && ls.getLastReaction() != -1 && Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - ls.getLastReaction()))) >= CloudManager.config.getTimeOutCheckTime()) {
                                    unregister(taskedService.getEntry().getServiceName());
                                }
                            }
                        });
                        CloudManager.serviceDriver.getServices().parallelStream().filter(taskedService -> taskedService.getEntry().getStatus() != ServiceState.QUEUED || taskedService.getEntry().getStatus() == ServiceState.STARTED).toList().forEach(taskedService -> {
                            if (taskedService.getProcess() != null &&taskedService.getProcess().getProcess() != null &&!taskedService.getProcess().getProcess().isAlive()){
                                unregister(taskedService.getEntry().getServiceName());
                            }
                        });
                    }
                }
            }, 0, 10, TimeUtil.SECONDS);


            /*
            *
            * Update to all services
            *
            * */
            new TimerBase().scheduleAsync(new TimerTask() {
                @Override
                public void run() {
                    if (!CloudManager.shutdown) {
                        services.stream()
                                .filter(taskedService -> taskedService.getEntry().getStatus() != ServiceState.STARTED && taskedService.getEntry().getStatus() != ServiceState.QUEUED)
                                .forEach(taskedService -> NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutServiceConnected(taskedService.getEntry().getServiceName(), taskedService.getEntry().getGroupName())));

                        PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                        general.getCloudplayers().forEach(s -> {
                            CloudPlayerRestCache restCech = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(s)), CloudPlayerRestCache.class);
                            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutPlayerConnect(s, restCech.getCloudplayerproxy()));
                        });
                    }
                }
            }, 0, 3, TimeUtil.MINUTES);


        });
        current.setPriority(1);
        current.start();
    }

    @Override
    public void updatePlayers(String service, boolean connect) {
        Objects.requireNonNull(this.services.stream().filter(service1 -> service1.getEntry().getServiceName().equals(service)).findFirst().orElse(null)).handelCloudPlayerConnection(connect);
    }

    @Override
    public TaskedService getService(String service) {
        return this.services.stream().noneMatch(service1 -> service1.getEntry().getServiceName().equals(service)) ? null : this.services.stream().filter(service1 -> service1.getEntry().getServiceName().equals(service)).findFirst().orElse(null);
    }

    @Override
    public ArrayDeque<TaskedService> getServices() {
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
