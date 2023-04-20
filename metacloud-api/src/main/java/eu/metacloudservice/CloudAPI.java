package eu.metacloudservice;

import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.bootstrap.bungee.listener.CloudEvents;
import eu.metacloudservice.bootstrap.bungee.networking.*;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.events.EventDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.*;
import eu.metacloudservice.networking.client.NettyClient;
import eu.metacloudservice.networking.in.service.PacketInServiceConnect;
import eu.metacloudservice.networking.in.service.cloudapi.*;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketOutAPIPlayerDispactchCommand;
import eu.metacloudservice.networking.out.service.PacketOutServiceConnected;
import eu.metacloudservice.networking.out.service.PacketOutServiceDisconnected;
import eu.metacloudservice.networking.out.service.PacketOutServiceLaunch;
import eu.metacloudservice.networking.out.service.PacketOutServicePrepared;
import eu.metacloudservice.networking.out.service.group.PacketOutGroupCreate;
import eu.metacloudservice.networking.out.service.group.PacketOutGroupDelete;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerConnect;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerDisconnect;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerSwitchService;
import eu.metacloudservice.networking.out.service.playerbased.apibased.*;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.pool.player.PlayerPool;
import eu.metacloudservice.pool.service.ServicePool;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.GroupList;
import eu.metacloudservice.webserver.dummys.WhiteList;
import lombok.NonNull;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class CloudAPI {

    private static CloudAPI instance;
    private boolean isVelo;
    private LiveService service;

    private PlayerPool playerPool;
    private ServicePool servicePool;
    private RestDriver restDriver;
    private EventDriver eventDriver;

    public CloudAPI(boolean isVelo) {
        instance = this;
        isVelo = isVelo;
        new Driver();
        service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        new NettyDriver();
        this.playerPool = new PlayerPool(service);
        this.servicePool = new ServicePool();

        restDriver = new RestDriver(service.getManagerAddress(), service.getRestPort());
        NettyDriver.getInstance().nettyClient = new NettyClient();
        NettyDriver.getInstance().nettyClient.bind(service.getManagerAddress(), service.getNetworkPort()).connect();


        NettyDriver.getInstance().packetDriver
                .registerHandler(new PacketOutServicePrepared().getPacketUUID(), new HandlePacketOutServicePrepared(), PacketOutServicePrepared.class)
                .registerHandler(new PacketOutServiceConnected().getPacketUUID(), new HandlePacketOutServiceConnected(), PacketOutServiceConnected.class)
                .registerHandler(new PacketOutServiceDisconnected().getPacketUUID(), new HandlePacketOutServiceDisconnected(), PacketOutServiceDisconnected.class)
                .registerHandler(new PacketOutPlayerConnect().getPacketUUID(), new HandlePacketOutPlayerConnect(), PacketOutPlayerConnect.class)
                .registerHandler(new PacketOutPlayerDisconnect().getPacketUUID(), new HandlePacketOutPlayerDisconnect(), PacketOutPlayerDisconnect.class)
                .registerHandler(new PacketOutPlayerSwitchService().getPacketUUID(), new HandlePacketOutPlayerSwitchService(), PacketOutPlayerSwitchService.class)
                .registerHandler(new PacketOutServiceLaunch().getPacketUUID(), new HandlePacketOutServiceLaunch(), PacketOutServiceLaunch.class)
                .registerHandler(new PacketOutGroupCreate().getPacketUUID(), new HandlePacketOutGroupCreate(), PacketOutGroupCreate.class)
                .registerHandler(new PacketOutGroupDelete().getPacketUUID(), new HandlePacketOutGroupDelete(), PacketOutGroupDelete.class);


        this.eventDriver = new EventDriver();

        Group group = (Group) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/" + service.getGroup()), Group.class);
        if (group.getGroupType().equals("PROXY")){
            if (isVelo){
                eventDriver.registerListener(new eu.metacloudservice.bootstrap.velocity.listener.CloudEvents());
                NettyDriver.getInstance().packetDriver
                        .registerHandler(new PacketOutAPIPlayerConnect().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerConnect(), PacketOutAPIPlayerConnect.class)
                        .registerHandler(new PacketOutAPIPlayerMessage().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerMessage(), PacketOutAPIPlayerMessage.class)
                        .registerHandler(new PacketOutAPIPlayerTitle().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerTitle(), PacketOutAPIPlayerTitle.class)
                        .registerHandler(new PacketOutAPIPlayerActionBar().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerActionBar(), PacketOutAPIPlayerActionBar.class)
                        .registerHandler(new PacketOutAPIPlayerDispactchCommand().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerDispactchCommand(), PacketOutAPIPlayerDispactchCommand.class)
                        .registerHandler(new PacketOutAPIPlayerKick().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerKick(), PacketOutAPIPlayerKick.class)
                        .registerHandler(new PacketOutAPIPlayerTab().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerTab(), PacketOutAPIPlayerTab.class);

            }else {
                eventDriver.registerListener(new CloudEvents());
                NettyDriver.getInstance().packetDriver
                        .registerHandler(new PacketOutAPIPlayerConnect().getPacketUUID(), new HandlePacketOutAPIPlayerConnect(), PacketOutAPIPlayerConnect.class)
                        .registerHandler(new PacketOutAPIPlayerMessage().getPacketUUID(), new HandlePacketOutAPIPlayerMessage(), PacketOutAPIPlayerMessage.class)
                        .registerHandler(new PacketOutAPIPlayerTitle().getPacketUUID(), new HandlePacketOutAPIPlayerTitle(), PacketOutAPIPlayerTitle.class)
                        .registerHandler(new PacketOutAPIPlayerActionBar().getPacketUUID(), new HandlePacketOutAPIPlayerActionBar(), PacketOutAPIPlayerActionBar.class)
                        .registerHandler(new PacketOutAPIPlayerKick().getPacketUUID(), new HandlePacketOutAPIPlayerKick(), PacketOutAPIPlayerKick.class)
                        .registerHandler(new PacketOutAPIPlayerDispactchCommand().getPacketUUID(), new HandlePacketOutAPIPlayerDispactchCommand(), PacketOutAPIPlayerDispactchCommand.class)
                        .registerHandler(new PacketOutAPIPlayerTab().getPacketUUID(), new HandlePacketOutAPIPlayerTab(), PacketOutAPIPlayerTab.class);
            }
        }


        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInServiceConnect(service.getService()));

    }


    public void launchService(String group){
        sendPacketSynchronized(new PacketInLaunchService(group));
    }

    public void launchServices(String group, int count){
        for (int i = 0; i != count-1; i++) {
            launchService(group);
        }
    }

    public void dispatchCommand(String command){
        sendPacketSynchronized(new PacketInDispatchMainCommand(command));
    }

    public void stopService(String service){
        sendPacketSynchronized(new PacketInStopService(service));
    }

    public void stopGroup(String group){
        sendPacketSynchronized(new PacketInStopGroup(group));
    }


    public EventDriver getEventDriver() {
        return eventDriver;
    }


    public ArrayList<Group> getGroups(){
        ArrayList<Group> groups = new ArrayList<>();
        GroupList cech = (GroupList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/general"), GroupList.class);
        cech.getGroups().forEach(s -> {
            Group g = (Group) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/" + s), Group.class);
            groups.add(g);
        });
        return groups;

    }

    public LiveService getCurrentService(){
        return service;
    }

    public List<String> getGroupsName(){
        GroupList cech = (GroupList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/general"), GroupList.class);
        return cech.getGroups();
    }

    public List<String> getWhitelist(){
        WhiteList cech = (WhiteList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/default/whitelist"), WhiteList.class);
        return cech.getWhitelist();
    }

    public Messages getMessages(){
        return (Messages) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/message/default"), Messages.class);
    }

    public boolean addWhiteList(String username){
        if (getWhitelist().stream().noneMatch(s -> s.equals(username))){
            dispatchCommand("service whitelist add " + username);
            return true;
        }
        return false;
    }

    public boolean removeWhiteList(String username){
        if (getWhitelist().stream().anyMatch(s -> s.equals(username))){
            dispatchCommand("service whitelist remove " + username);
            return true;
        }

        return false;
    }

    public void registerListener(ICloudListener eventListener){
        eventDriver.registerListener(eventListener);
    }

    public void setState(@NonNull ServiceState state, String name){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInChangeState(name, state.toString()));
    }

    public void setState(ServiceState state){
        setState(state, getCurrentService().getService());
    }
    
    public double getUsedMemory(){
        return  ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1048576;

    }

    public double getMaxMemory(){
        return  ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() / 1048576;

    }
    public AsyncCloudAPI getAsyncAPI(){
        return AsyncCloudAPI.getInstance();
    }
    public void sendPacketSynchronized(Packet packet){
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(packet);
    }
    

    public RestDriver getRestDriver() {
        return restDriver;
    }

    public PlayerPool getPlayerPool() {
        return playerPool;
    }

    public ServicePool getServicePool() {
        return servicePool;
    }

    public static CloudAPI getInstance() {
        return instance;
    }
}
