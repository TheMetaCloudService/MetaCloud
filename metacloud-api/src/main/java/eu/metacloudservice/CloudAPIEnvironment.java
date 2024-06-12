package eu.metacloudservice;

import eu.metacloudservice.bootstrap.bungee.listener.CloudEvents;
import eu.metacloudservice.bootstrap.bungee.networking.*;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.networking.*;
import eu.metacloudservice.networking.client.NettyClient;
import eu.metacloudservice.networking.packet.packets.in.service.PacketInServiceConnect;
import eu.metacloudservice.networking.packet.packets.out.service.*;
import eu.metacloudservice.networking.packet.packets.out.service.events.*;
import eu.metacloudservice.networking.packet.packets.out.service.group.PacketOutGroupCreate;
import eu.metacloudservice.networking.packet.packets.out.service.group.PacketOutGroupDelete;
import eu.metacloudservice.networking.packet.packets.out.service.group.PacketOutGroupEdit;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.PacketOutPlayerConnect;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.PacketOutPlayerDisconnect;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.PacketOutPlayerSwitchService;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.*;
import eu.metacloudservice.player.async.entrys.AsyncCloudPlayer;
import eu.metacloudservice.player.entrys.CloudPlayer;
import eu.metacloudservice.service.entrys.CloudService;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import eu.metacloudservice.webserver.dummys.PlayerGeneral;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServiceList;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServices;

import java.util.Objects;
import java.util.TimerTask;

public class CloudAPIEnvironment {


    public CloudAPIEnvironment() {}

    public void handleNettyConnection(){
        new NettyDriver();
        NettyDriver.getInstance().nettyClient = new NettyClient();
        NettyDriver.getInstance().nettyClient.bind(CloudAPI.getInstance().getService().getManagerAddress(), CloudAPI.getInstance().getService().getNetworkPort()).connect();

        PlayerGeneral players = (PlayerGeneral) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/genernal"), PlayerGeneral.class);
        players.getCloudplayers().forEach(s -> {
            if (!CloudAPI.getInstance().getPlayerPool().playerIsNotNull(s)){
                CloudAPI.getInstance().getAsyncPlayerPool().registerPlayer(new AsyncCloudPlayer(s, Objects.requireNonNull(UUIDDriver.getUUID(s))));
                CloudAPI.getInstance().getPlayerPool().registerPlayer(new CloudPlayer(s, Objects.requireNonNull(UUIDDriver.getUUID(s))));
            }
        });
    }

    public void handelNettyUpdate(){
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInServiceConnect(CloudAPI.getInstance().getService().getService()));

        new TimerBase().schedule(new TimerTask() {
            @Override
            public void run() {
                CloudService service = CloudAPI.getInstance().getServicePool().getService(CloudAPI.getInstance().getCurrentService().getService());
                LiveServiceList list = (LiveServiceList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudservice/general"), LiveServiceList.class);
                PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/genernal"), PlayerGeneral.class);
                general.getCloudplayers().forEach(s -> {
                    if (!CloudAPI.getInstance().getPlayerPool().playerIsNotNull(s)){
                        CloudAPI.getInstance().getAsyncPlayerPool().registerPlayer(new AsyncCloudPlayer(s, Objects.requireNonNull(UUIDDriver.getUUID(s))));
                        CloudAPI.getInstance().getPlayerPool().registerPlayer(new CloudPlayer(s, Objects.requireNonNull(UUIDDriver.getUUID(s))));
                    }
                });
                CloudAPI.getInstance().getPlayerPool().getPlayers().stream().filter(cloudPlayer -> general.getCloudplayers().stream().noneMatch(s -> s.equalsIgnoreCase(cloudPlayer.getUniqueId().toString()))).toList().forEach(cloudPlayer -> {
                    CloudAPI.getInstance().getPlayerPool().unregisterPlayer(cloudPlayer.getUniqueId());
                    CloudAPI.getInstance().getAsyncPlayerPool().unregisterPlayer(cloudPlayer.getUniqueId());
                });
                CloudAPI.getInstance().getServicePool().getServices().stream().filter(cloudService -> list.getCloudServices().stream().noneMatch(s -> s.equalsIgnoreCase(cloudService.getName()))).toList().forEach(cloudService -> {
                    CloudAPI.getInstance().getServicePool().unregisterService(cloudService.getName());
                    CloudAPI.getInstance().getAsyncServicePool().unregisterService(cloudService.getName());
                });
                if (!NettyDriver.getInstance().nettyClient.getChannel().isOpen()){
                    System.exit(0);
                }
                LiveServices liveServices = (LiveServices) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudservice/" + service.getName().replace(list.getCloudServiceSplitter(), "~")), LiveServices.class);
                liveServices.setLastReaction(System.currentTimeMillis());
                CloudAPI.getInstance().getRestDriver().update("/cloudservice/" + service.getName().replace(list.getCloudServiceSplitter(), "~"), new ConfigDriver().convert(liveServices));

            }
        }, 30, 30, TimeUtil.SECONDS);
    }




    public void  registerHandlers(){
        NettyDriver.getInstance().getPacketDriver()
                .registerHandler(new PacketOutCloudServiceCouldNotStartEvent().getPacketUUID(), new HandlePacketOutCloudServiceCouldNotStartEvent(), PacketOutCloudServiceCouldNotStartEvent.class)
                .registerHandler(new PacketOutCloudProxyCouldNotStartEvent().getPacketUUID(), new HandlePacketOutCloudProxyCouldNotStartEvent(), PacketOutCloudProxyCouldNotStartEvent.class)
                .registerHandler(new PacketOutServicePrepared().getPacketUUID(), new HandlePacketOutServicePrepared(), PacketOutServicePrepared.class)
                .registerHandler(new PacketOutServiceConnected().getPacketUUID(), new HandlePacketOutServiceConnected(), PacketOutServiceConnected.class)
                .registerHandler(new PacketOutServiceDisconnected().getPacketUUID(), new HandlePacketOutServiceDisconnected(), PacketOutServiceDisconnected.class)
                .registerHandler(new PacketOutPlayerConnect().getPacketUUID(), new HandlePacketOutPlayerConnect(), PacketOutPlayerConnect.class)
                .registerHandler(new PacketOutPlayerDisconnect().getPacketUUID(), new HandlePacketOutPlayerDisconnect(), PacketOutPlayerDisconnect.class)
                .registerHandler(new PacketOutPlayerSwitchService().getPacketUUID(), new HandlePacketOutPlayerSwitchService(), PacketOutPlayerSwitchService.class)
                .registerHandler(new PacketOutServiceLaunch().getPacketUUID(), new HandlePacketOutServiceLaunch(), PacketOutServiceLaunch.class)
                .registerHandler(new PacketOutGroupCreate().getPacketUUID(), new HandlePacketOutGroupCreate(), PacketOutGroupCreate.class)
                .registerHandler(new PacketOutGroupDelete().getPacketUUID(), new HandlePacketOutGroupDelete(), PacketOutGroupDelete.class)
                .registerHandler(new PacketOutGroupEdit().getPacketUUID(), new HandlePacketOutGroupEdit(), PacketOutGroupEdit.class)

                .registerHandler(new PacketOutCloudRestAPIReloadEvent().getPacketUUID(), new HandlePacketOutCloudRestAPIReloadEvent(), PacketOutCloudRestAPIReloadEvent.class)
                .registerHandler(new PacketOutCloudRestAPICreateEvent().getPacketUUID(), new HandlePacketOutCloudRestAPICreateEvent(), PacketOutCloudRestAPICreateEvent.class)
                .registerHandler(new PacketOutCloudRestAPIUpdateEvent().getPacketUUID(), new HandlePacketOutCloudRestAPIUpdateEvent(), PacketOutCloudRestAPIUpdateEvent.class)
                .registerHandler(new PacketOutCloudRestAPIDeleteEvent().getPacketUUID(), new HandlePacketOutCloudRestAPIDeleteEvent(), PacketOutCloudRestAPIDeleteEvent.class)


                .registerHandler(new PacketOutCloudServiceChangeState().getPacketUUID(), new HandlePacketOutCloudServiceChangeState(), PacketOutCloudServiceChangeState.class)
                .registerHandler(new PacketOutCloudProxyChangeState().getPacketUUID(), new HandlePacketOutCloudProxyChangeState(), PacketOutCloudProxyChangeState.class);
    }

    public void registerVelocityHandlers() {
        CloudAPI.getInstance().getEventDriver().registerListener(new eu.metacloudservice.bootstrap.velocity.listener.CloudEvents());
        NettyDriver.getInstance().getPacketDriver()
                .registerHandler(new PacketOutAPIPlayerConnect().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerConnect(), PacketOutAPIPlayerConnect.class)
                .registerHandler(new PacketOutAPIPlayerMessage().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerMessage(), PacketOutAPIPlayerMessage.class)
                .registerHandler(new PacketOutAPIPlayerTitle().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerTitle(), PacketOutAPIPlayerTitle.class)
                .registerHandler(new PacketOutAPIPlayerActionBar().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerActionBar(), PacketOutAPIPlayerActionBar.class)
                .registerHandler(new PacketOutAPIPlayerDispactchCommand().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerDispactchCommand(), PacketOutAPIPlayerDispactchCommand.class)
                .registerHandler(new PacketOutAPIPlayerKick().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerKick(), PacketOutAPIPlayerKick.class)
                .registerHandler(new PacketOutCloudPlayerComponent().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutCloudPlayerComponent(), PacketOutCloudPlayerComponent.class)
                .registerHandler(new PacketOutAPIPlayerTab().getPacketUUID(), new eu.metacloudservice.bootstrap.velocity.networking.HandlePacketOutAPIPlayerTab(), PacketOutAPIPlayerTab.class);

    }

    public void registerBungeeHandlers() {
        CloudAPI.getInstance().getEventDriver().registerListener(new CloudEvents());
        NettyDriver.getInstance().getPacketDriver()
                .registerHandler(new PacketOutAPIPlayerConnect().getPacketUUID(), new HandlePacketOutAPIPlayerConnect(), PacketOutAPIPlayerConnect.class)
                .registerHandler(new PacketOutAPIPlayerMessage().getPacketUUID(), new HandlePacketOutAPIPlayerMessage(), PacketOutAPIPlayerMessage.class)
                .registerHandler(new PacketOutAPIPlayerTitle().getPacketUUID(), new HandlePacketOutAPIPlayerTitle(), PacketOutAPIPlayerTitle.class)
                .registerHandler(new PacketOutAPIPlayerActionBar().getPacketUUID(), new HandlePacketOutAPIPlayerActionBar(), PacketOutAPIPlayerActionBar.class)
                .registerHandler(new PacketOutAPIPlayerKick().getPacketUUID(), new HandlePacketOutAPIPlayerKick(), PacketOutAPIPlayerKick.class)
                .registerHandler(new PacketOutCloudPlayerComponent().getPacketUUID(), new HandlePacketOutCloudPlayerComponent(), PacketOutCloudPlayerComponent.class)
                .registerHandler(new PacketOutAPIPlayerDispactchCommand().getPacketUUID(), new HandlePacketOutAPIPlayerDispactchCommand(), PacketOutAPIPlayerDispactchCommand.class)
                .registerHandler(new PacketOutAPIPlayerTab().getPacketUUID(), new HandlePacketOutAPIPlayerTab(), PacketOutAPIPlayerTab.class);
    }
}
