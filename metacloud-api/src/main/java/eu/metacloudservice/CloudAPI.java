package eu.metacloudservice;

import eu.metacloudservice.bootstrap.bungee.listener.CloudEvents;
import eu.metacloudservice.bootstrap.bungee.networking.HandlePacketOutAPIPlayerConnect;
import eu.metacloudservice.bootstrap.bungee.networking.HandlePacketOutAPIPlayerKick;
import eu.metacloudservice.bootstrap.bungee.networking.HandlePacketOutAPIPlayerMessage;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.events.EventDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.*;
import eu.metacloudservice.networking.client.NettyClient;
import eu.metacloudservice.networking.in.service.PacketInServiceConnect;
import eu.metacloudservice.networking.in.service.PacketInServiceDisconnect;
import eu.metacloudservice.networking.in.service.cloudapi.*;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerConnect;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerDisconnect;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerSwitchService;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketInAPIPlayerConnect;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketInAPIPlayerKick;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketInAPIPlayerMessage;
import eu.metacloudservice.networking.out.service.PacketOutServiceConnected;
import eu.metacloudservice.networking.out.service.PacketOutServiceDisconnected;
import eu.metacloudservice.networking.out.service.PacketOutServicePrepared;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerConnect;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerDisconnect;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerSwitchService;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerConnect;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerKick;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerMessage;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.pool.player.PlayerPool;
import eu.metacloudservice.pool.service.ServicePool;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.WhiteList;
import lombok.NonNull;

import java.util.List;

public class CloudAPI {

    private static CloudAPI instance;
    private LiveService service;

    private PlayerPool playerPool;
    private ServicePool servicePool;
    private RestDriver restDriver;
    private EventDriver eventDriver;

    public CloudAPI() {
        instance = this;
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
                        .registerPacket(PacketInAPIPlayerMessage.class)
                        .registerPacket(PacketInAPIPlayerConnect.class)
                        .registerPacket(PacketInAPIPlayerKick.class)
                        .registerPacket(PacketInDispatchMainCommand.class)
                        .registerPacket(PacketInLaunchService.class)
                        .registerPacket(PacketInStopGroup.class)
                        .registerPacket(PacketInStopService.class)
                        .registerPacket(PacketInChangeState.class)
                        .registerPacket(PacketInDispatchCommand.class)
                        .registerPacket(PacketInPlayerConnect.class)
                        .registerPacket(PacketInPlayerDisconnect.class)
                        .registerPacket(PacketInPlayerSwitchService.class)
                        .registerPacket(PacketInServiceDisconnect.class)
                        .registerPacket(PacketInServiceConnect.class);


        this.eventDriver = new EventDriver();

        Group group = (Group) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/groups/" + service.getGroup()), Group.class);
        if (group.getGroupType().equals("PROXY")){
            eventDriver.registerListener(new CloudEvents());
            NettyDriver.getInstance().packetDriver
                    .registerHandler(new PacketOutAPIPlayerConnect().getPacketUUID(), new HandlePacketOutAPIPlayerConnect(), PacketOutAPIPlayerConnect.class)
                    .registerHandler(new PacketOutAPIPlayerMessage().getPacketUUID(), new HandlePacketOutAPIPlayerMessage(), PacketOutAPIPlayerMessage.class)
                    .registerHandler(new PacketOutAPIPlayerKick().getPacketUUID(), new HandlePacketOutAPIPlayerKick(), PacketOutAPIPlayerKick.class);

        }





        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInServiceConnect(service.getService()));

    }


    public void launchService(String group){
        sendPacketSynchronized(new PacketInLaunchService(group));
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

    public List<String> getWhitelist(){
        WhiteList cech = (WhiteList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/general/whitelist"), WhiteList.class);
        return cech.getWhitelist();
    }

    public Messages getMessages(){
        return (Messages) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/general/messages"), Messages.class);
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


    public void sendPacketSynchronized(Packet packet){
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(packet);
    }

    public void sendPacketAsynchronous(Packet packet){
        NettyDriver.getInstance().nettyClient.sendPacketsAsynchronous(packet);
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
