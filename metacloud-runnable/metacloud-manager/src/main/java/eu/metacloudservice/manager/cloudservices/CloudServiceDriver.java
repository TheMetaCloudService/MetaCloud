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
import eu.metacloudservice.manager.cloudservices.entry.NetworkEntry;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.manager.cloudservices.entry.TaskedService;
import eu.metacloudservice.manager.cloudservices.interfaces.ICloudServiceDriver;
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
        Thread thread = new CloudServiceWorker();
        thread.start();
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
