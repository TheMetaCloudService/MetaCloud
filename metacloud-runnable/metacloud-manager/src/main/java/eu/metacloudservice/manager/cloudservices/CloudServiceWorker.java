package eu.metacloudservice.manager.cloudservices;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.listeners.services.CloudProxyCouldNotStartEvent;
import eu.metacloudservice.events.listeners.services.CloudProxyLaunchEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceCouldNotStartEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceLaunchEvent;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.manager.cloudservices.entry.TaskedService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.out.service.PacketOutServiceConnected;
import eu.metacloudservice.networking.packet.packets.out.service.PacketOutServiceLaunch;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudProxyCouldNotStartEvent;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudServiceCouldNotStartEvent;
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

public class CloudServiceWorker extends Thread{


    public CloudServiceWorker(){
        this.setPriority(1);
    }

    @Override
    public void run() {

        /*
        * cloudQueue - Handling the starts and stops from services in the Cloud
         * */
        new TimerBase().scheduleAsync(new TimerTask() {
            @Override
            public void run() {
                    final int cpuLoad = Driver.getInstance().getMessageStorage().getCPULoad();
                    final int maxCpuUsage = CloudManager.config.getProcessorUsage();
                    if (cpuLoad <= maxCpuUsage) {
                        handleQueueStartServicesIfNecessary(); // Handling the StartQueue
                        handleQueuestopServicesIfNecessary(); // Handling the StopQueue
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
                ArrayDeque<TaskedService> currentServices = CloudManager.serviceDriver.getServices();
                CloudManager.serviceDriver.entry.global_players = currentServices.stream()
                        .mapToInt(s -> s.getEntry().getCurrentPlayers())
                        .sum();
                CloudManager.serviceDriver.entry.global_player_potency = CloudManager.serviceDriver.entry.global_players / 100;
                HashMap<String, Integer> groupPlayerPotency = groups.parallelStream()
                        .map(g -> CloudManager.serviceDriver.getServices(g.getGroup()))
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
                CloudManager.serviceDriver. entry.group_player_potency.putAll(groupPlayerPotency);


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
                    List<TaskedService> services = CloudManager.serviceDriver.getServices().stream()
                            .filter(taskedService -> taskedService.getEntry().getStatus() == ServiceState.LOBBY)
                            .filter(taskedService -> taskedService.getEntry().getCurrentPlayers() == 0)
                            .filter(taskedService -> Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - taskedService.getEntry().getTime()))) >= 120)
                            .toList();

                    services.forEach(taskedService -> {
                        Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());
                        int online = (!CloudManager.serviceDriver.entry.group_player_potency.containsKey(group.getGroup())) ? group.getMinimalOnline()
                                : (CloudManager.serviceDriver.entry.group_player_potency.get(group.getGroup()) == 0 && CloudManager.serviceDriver.entry.global_player_potency == 0) ? group.getMinimalOnline()
                                : (CloudManager.serviceDriver.entry.global_player_potency != 0) ? group.getOver100AtNetwork() * CloudManager.serviceDriver.entry.global_player_potency
                                : group.getOver100AtGroup() * CloudManager.serviceDriver.entry.group_player_potency.get(group.getGroup());

                        if (taskedService.hasStartedNew) {
                            CloudManager.serviceDriver.  unregister(taskedService.getEntry().getServiceName());
                        }

                        if (CloudManager.serviceDriver.getLobbiedServices(taskedService.getEntry().getGroupName()) - 1 >= online) {
                            CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName());
                        }
                    });

                    List<TaskedService> servicess = CloudManager.serviceDriver.getServices().stream()
                            .filter(taskedService -> taskedService.getEntry().getStatus() == ServiceState.LOBBY)
                            .toList();

                    servicess.forEach(taskedService -> {
                        Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());
                        int minonline = (!CloudManager.serviceDriver.entry.group_player_potency.containsKey(group.getGroup())) ? group.getMinimalOnline()
                                : (CloudManager.serviceDriver.entry.group_player_potency.get(group.getGroup()) == 0 && CloudManager.serviceDriver.entry.global_player_potency == 0) ? group.getMinimalOnline()
                                : (CloudManager.serviceDriver.entry.global_player_potency != 0) ? group.getOver100AtNetwork() * CloudManager.serviceDriver.entry.global_player_potency
                                : group.getOver100AtGroup() * CloudManager.serviceDriver.entry.group_player_potency.get(group.getGroup());

                        int inStoppedQueue = CloudManager.queueDriver.getQueue_shutdown().stream()
                                .filter(s -> CloudManager.serviceDriver.getService(s).getEntry().getGroupName().equalsIgnoreCase(taskedService.getEntry().getGroupName()))
                                .toList().size();

                        if (CloudManager.serviceDriver.getLobbiedServices(taskedService.getEntry().getGroupName()) - 1 - inStoppedQueue >= minonline) {
                            CloudManager.serviceDriver. unregister(taskedService.getEntry().getServiceName());
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
                                String id = CloudManager.config.getUuid().equals("INT") ? String.valueOf(CloudManager.serviceDriver.getFreeUUID(taskedService.getEntry().getGroupName())) : CloudManager.serviceDriver.getFreeUUID();
                                if (taskedService.getEntry().getNode().equals("InternalNode")) {
                                    CloudManager.serviceDriver.register(new TaskedEntry(
                                            CloudManager.serviceDriver.getFreePort(Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName()).getGroupType().equalsIgnoreCase("PROXY")),
                                            taskedService.getEntry().getGroupName(),
                                            taskedService.getEntry().getGroupName() + CloudManager.config.getSplitter() + id,
                                            "InternalNode", taskedService.getEntry().isUseProtocol(), id, false, ""));
                                } else {
                                    CloudManager.serviceDriver.register(new TaskedEntry(
                                            -1,
                                            taskedService.getEntry().getGroupName(),
                                            taskedService.getEntry().getGroupName() + CloudManager.config.getSplitter() + id,
                                            taskedService.getEntry().getNode(), taskedService.getEntry().isUseProtocol(), id, false, ""));
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
                        .filter(group -> (!CloudManager.serviceDriver.entry.group_player_potency.containsKey(group.getGroup())) ? (  CloudManager.serviceDriver.getActiveServices(group.getGroup()) < group.getMinimalOnline()) : ((CloudManager.serviceDriver.entry.group_player_potency.get(group.getGroup()) == 0 && CloudManager.serviceDriver.entry.global_player_potency == 0) ? (CloudManager.serviceDriver.getActiveServices(group.getGroup()) < group.getMinimalOnline()) : ((CloudManager.serviceDriver.entry.global_player_potency != 0) ? (CloudManager.serviceDriver.getActiveServices(group.getGroup()) < group.getOver100AtNetwork() * CloudManager.serviceDriver.entry.global_player_potency) : (CloudManager.serviceDriver.getActiveServices(group.getGroup()) < group.getOver100AtGroup() * CloudManager.serviceDriver.entry.group_player_potency.get(group.getGroup())))))
                        .filter(group -> CloudManager.serviceDriver.getServices(group.getGroup()).size() + 1 <= Integer.parseInt(String.valueOf(group.getMaximalOnline()).replace("-1", String.valueOf(Integer.MAX_VALUE))) )
                        .filter(group -> NettyDriver.getInstance().nettyServer.isChannelFound(group.getStorage().getRunningNode()) || group.getStorage().getRunningNode().equals("InternalNode"))
                        .sorted(Comparator.comparingInt(Group::getStartPriority).reversed())
                        .forEach(group -> {
                            int online = (!  CloudManager.serviceDriver.entry.group_player_potency.containsKey(group.getGroup())) ? group.getMinimalOnline() : (CloudManager.serviceDriver.entry.group_player_potency.get(group.getGroup()) == 0 && CloudManager.serviceDriver.entry.global_player_potency == 0) ? group.getMinimalOnline() : (CloudManager.serviceDriver.entry.global_player_potency != 0) ? group.getOver100AtNetwork() * CloudManager.serviceDriver.entry.global_player_potency : group.getOver100AtGroup() * CloudManager.serviceDriver.entry.group_player_potency.get(group.getGroup());
                            if (!CloudManager.serviceDriver.delete.contains(group.getGroup())) {
                                int minimal = online - CloudManager.serviceDriver.getActiveServices(group.getGroup());
                                for (int i = 0; i != minimal; i++) {
                                    String id = CloudManager.config.getUuid().equals("INT") ? String.valueOf(CloudManager.serviceDriver.getFreeUUID(group.getGroup())) : CloudManager.serviceDriver.getFreeUUID();
                                    String entryName = group.getGroup() + CloudManager.config.getSplitter() + id;
                                    String node = group.getStorage().getRunningNode();
                                    int freePort = CloudManager.serviceDriver.getFreePort(group.getGroupType().equalsIgnoreCase("PROXY"));
                                    int memoryAfter = Driver.getInstance().getMessageStorage().canUseMemory - group.getUsedMemory();

                                    if (node.equals("InternalNode") && memoryAfter >= 0) {
                                        CloudManager.serviceDriver.register(new TaskedEntry(freePort, group.getGroup(), entryName, node, CloudManager.config.getUseProtocol(), id, false, ""));
                                        Driver.getInstance().getMessageStorage().canUseMemory = memoryAfter;
                                    } else if (!node.equals("InternalNode")) {
                                        CloudManager.serviceDriver.register(new TaskedEntry(-1, group.getGroup(), entryName, node, CloudManager.config.getUseProtocol(), id, false, ""));
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
                                CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName());
                            }
                        }
                    });
                    CloudManager.serviceDriver.getServices().parallelStream().filter(taskedService -> taskedService.getEntry().getStatus() != ServiceState.QUEUED || taskedService.getEntry().getStatus() == ServiceState.STARTED).toList().forEach(taskedService -> {
                        if (taskedService.getProcess() != null && taskedService.getProcess().getProcess() != null &&!taskedService.getProcess().getProcess().isAlive()){
                            CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName());
                            if (Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName()).getGroupType().equalsIgnoreCase("PROXY")){
                                Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudProxyCouldNotStartEvent(taskedService.getEntry().getServiceName()));
                                NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutCloudProxyCouldNotStartEvent(taskedService.getEntry().getServiceName()));
                            }else {
                                Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudServiceCouldNotStartEvent(taskedService.getEntry().getServiceName()));

                                NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutCloudServiceCouldNotStartEvent(taskedService.getEntry().getServiceName()));


                            }
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
                    CloudManager.serviceDriver.getServices().stream()
                            .filter(taskedService -> taskedService.getEntry().getStatus() != ServiceState.STARTED && taskedService.getEntry().getStatus() != ServiceState.QUEUED)
                            .forEach(taskedService -> NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutServiceConnected(taskedService.getEntry().getServiceName(), taskedService.getEntry().getGroupName())));

                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    general.getCloudplayers().forEach(s -> {
                        CloudPlayerRestCache restCech = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + s), CloudPlayerRestCache.class);

                        NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutPlayerConnect(UUIDDriver.getUsername(UUID.fromString(s)), restCech.getCloudplayerproxy()));
                    });
                }
            }
        }, 0, 3, TimeUtil.MINUTES);


    }



    private void handleQueueStartServicesIfNecessary(){
        final int runningServices = CloudManager.serviceDriver.getServices().stream()
                .filter(ts -> ts.getEntry().getStatus() == ServiceState.STARTED).toList().size();
        final int maxServices =  CloudManager.config.getServiceStartupCount();
        if (runningServices <= maxServices && !CloudManager.queueDriver.getQueue_startup().isEmpty()) {
            final String service = CloudManager.queueDriver.getQueue_startup().removeFirst();
             Optional<TaskedService> serviceOptional = Optional.ofNullable(CloudManager.serviceDriver.getService(service));
            serviceOptional.ifPresent(serviceInstance -> {
                String groupName = serviceInstance.getEntry().getGroupName();
                String node = serviceInstance.getEntry().getNode();
                Driver.getInstance().getMessageStorage().eventDriver.executeEvent(Driver.getInstance().getGroupDriver().load(groupName).getGroupType().equalsIgnoreCase("PROXY") ? new CloudProxyLaunchEvent(service, groupName, node) : new CloudServiceLaunchEvent(service, groupName, node));
                NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutServiceLaunch(service, Driver.getInstance().getGroupDriver().load(groupName).getGroupType().equalsIgnoreCase("PROXY"), groupName, node));

                serviceInstance.handelStatusChange(ServiceState.STARTED);
                serviceInstance.handelLaunch();
            });
        }
    }

    private void handleQueuestopServicesIfNecessary(){
        if (!CloudManager.queueDriver.getQueue_shutdown().isEmpty()) {
            String service = CloudManager.queueDriver.getQueue_shutdown().removeFirst();

            Optional<TaskedService> serviceOptional = Optional.ofNullable(CloudManager.serviceDriver.getService(service));
            serviceOptional.ifPresent(serviceInstance -> {
                serviceInstance.handelQuit();
                CloudManager.serviceDriver.unregistered(service);
            });
        }
    }


}
