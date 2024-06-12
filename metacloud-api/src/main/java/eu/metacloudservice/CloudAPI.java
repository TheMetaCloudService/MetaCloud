package eu.metacloudservice;

import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.events.EventDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.group.GroupPool;
import eu.metacloudservice.group.async.AsyncGroupPool;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInChangeState;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInDispatchMainCommand;
import eu.metacloudservice.networking.packet.packets.in.service.command.PacketInCommandWhitelist;
import eu.metacloudservice.offlineplayer.OfflinePlayerPool;
import eu.metacloudservice.offlineplayer.async.AsyncOfflinePlayerPool;
import eu.metacloudservice.player.PlayerPool;
import eu.metacloudservice.player.async.AsyncPlayerPool;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.service.ServicePool;
import eu.metacloudservice.service.async.AsyncServicePool;
import eu.metacloudservice.service.entrys.CloudService;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.WhiteList;
import lombok.Getter;
import lombok.NonNull;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public class CloudAPI {

    @Getter
    private static CloudAPI instance;
    private LiveService service;
    private PlayerPool playerPool;
    private OfflinePlayerPool offlinePlayerPool;
    private AsyncOfflinePlayerPool asyncOfflinePlayerPool;
    private GroupPool groupPool;
    private ServicePool servicePool;
    private RestDriver restDriver;
    private EventDriver eventDriver;
    private AsyncPlayerPool asyncPlayerPool;
    private AsyncServicePool asyncServicePool;
    private AsyncGroupPool asyncGroupPool;

    public CloudAPI() {
        instance = this;
        initializeCoreComponents();
        CompletableFuture.runAsync(this::initializeLazyComponents);
    }

    private void initializeCoreComponents() {
        new Driver();
        service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        restDriver = new RestDriver(service.getManagerAddress(), service.getRestPort());
    }

    private synchronized void initializeLazyComponents() {
        if (offlinePlayerPool == null) {
            offlinePlayerPool = new OfflinePlayerPool();
        }
        if (asyncOfflinePlayerPool == null) {
            asyncOfflinePlayerPool = new AsyncOfflinePlayerPool();
        }
        if (playerPool == null) {
            playerPool = new PlayerPool();
        }
        if (servicePool == null) {
            servicePool = new ServicePool();
        }
        if (groupPool == null) {
            groupPool = new GroupPool();
        }
        if (asyncGroupPool == null) {
            asyncGroupPool = new AsyncGroupPool();
        }
        if (asyncServicePool == null) {
            asyncServicePool = new AsyncServicePool();
        }
        if (asyncPlayerPool == null) {
            asyncPlayerPool = new AsyncPlayerPool();
        }
        if (eventDriver == null) {
            eventDriver = new EventDriver();
        }
    }

    public void dispatchCommand(String command){
        sendPacketSynchronized(new PacketInDispatchMainCommand(command));
    }

    public LiveService getCurrentService(){
        return service;
    }

    public CloudService getThisService(){
        initializeLazyComponents();
        return getServicePool().getService(getCurrentService().getService());
    }

    public CompletableFuture<CloudService> getThisServiceAsync(){
        return CompletableFuture.supplyAsync(this::getThisService);
    }

    public List<String> getWhitelist(){
        initializeLazyComponents();
        WhiteList cech = (WhiteList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/default/whitelist"), WhiteList.class);
        return cech.getWhitelist();
    }

    public Messages getMessages(){
        initializeLazyComponents();
        return (Messages) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/message/default"), Messages.class);
    }

    public boolean addWhiteList(String username){
        initializeLazyComponents();
        if (getWhitelist().stream().noneMatch(s -> s.equals(username))){
            CloudAPI.getInstance().sendPacketSynchronized(new PacketInCommandWhitelist(username));
            return true;
        }
        return false;
    }

    public boolean removeWhiteList(String username){
        initializeLazyComponents();
        if (getWhitelist().stream().anyMatch(s -> s.equals(username))){
            if (getPlayerPool().getPlayer(username) != null) getPlayerPool().getPlayer(username).disconnect(getMessages().getMessages().get("kickNetworkIsMaintenance"));
            CloudAPI.getInstance().sendPacketSynchronized(new PacketInCommandWhitelist(username));
            return true;
        }
        return false;
    }

    public void registerListener(ICloudListener eventListener){
        initializeLazyComponents();
        eventDriver.registerListener(eventListener);
    }

    public void setState(@NonNull ServiceState state, String name){
        initializeLazyComponents();
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInChangeState(name, state.toString()));
    }

    public void setState(ServiceState state){
        setState(state, getCurrentService().getService());
    }

    public double getUsedMemory(){
        return (double) ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1048576;
    }

    public double getMaxMemory(){
        return (double) ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() / 1048576;
    }

    public void sendPacketSynchronized(Packet packet){
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(packet);
    }

    public void sendPacketAsynchronous(Packet packet){
        NettyDriver.getInstance().nettyClient.sendPacketsAsynchronous(packet);
    }
}