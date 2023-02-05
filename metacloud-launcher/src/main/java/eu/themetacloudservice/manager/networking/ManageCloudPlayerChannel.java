package eu.themetacloudservice.manager.networking;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.events.dummys.cloudplayerbased.CloudPlayerConnectEvent;
import eu.themetacloudservice.events.dummys.cloudplayerbased.CloudPlayerDisconnectEvent;
import eu.themetacloudservice.events.dummys.cloudplayerbased.CloudPlayerSwitchServiceEvent;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerChangeService;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerConnect;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerDisconnect;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.webserver.RestDriver;
import eu.themetacloudservice.webserver.dummys.CloudPlayer;
import eu.themetacloudservice.webserver.entry.RouteEntry;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.UUID;

public class ManageCloudPlayerChannel implements IPacketListener {


    private ArrayList<String> disconnected;

    public ManageCloudPlayerChannel() {
        disconnected = new ArrayList<>();
    }

    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {


        if (paramPacket instanceof PackageCloudPlayerConnect){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '§f"+((PackageCloudPlayerConnect) paramPacket).getName()+"@"+((PackageCloudPlayerConnect) paramPacket).getUuid()+"§r' ist mit dem Proxy '§f"+((PackageCloudPlayerConnect) paramPacket).getProxy()+"§r' verbunden",
                    "The player '§f"+((PackageCloudPlayerConnect) paramPacket).getName()+"@"+((PackageCloudPlayerConnect) paramPacket).getUuid()+"§r' is connected to the proxy '§f"+((PackageCloudPlayerConnect) paramPacket).getProxy()+"§r'");

            CloudPlayer cloudPlayer = new CloudPlayer();
            cloudPlayer.setCurrentProxy(((PackageCloudPlayerConnect) paramPacket).getProxy());
            cloudPlayer.setUsername(((PackageCloudPlayerConnect) paramPacket).getName());
            cloudPlayer.setCurrentServer("");
            cloudPlayer.setUuid(((PackageCloudPlayerConnect) paramPacket).getUuid());
            CloudManager.serviceDriver.getService(((PackageCloudPlayerConnect) paramPacket).getProxy()).handelCloudPlayerConnection(true);
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/cloudplayer~" + cloudPlayer.getUsername(), new ConfigDriver().convert(cloudPlayer)));
            NettyDriver.getInstance().nettyServer.sendToAllPackets(paramPacket);

            Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerConnectEvent(((PackageCloudPlayerConnect) paramPacket).getName(), ((PackageCloudPlayerConnect) paramPacket).getProxy(), ((PackageCloudPlayerConnect) paramPacket).getUuid()));
        }

        if (paramPacket instanceof PackageCloudPlayerDisconnect){
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            disconnected.add(((PackageCloudPlayerDisconnect) paramPacket).getName());
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '§f"+((PackageCloudPlayerDisconnect) paramPacket).getName()+"@"+((PackageCloudPlayerDisconnect) paramPacket).getUuid()+"§r' hat das Netzwerk verlassen", "The player '§f"+((PackageCloudPlayerDisconnect) paramPacket).getName()+"@"+((PackageCloudPlayerDisconnect) paramPacket).getUuid()+"§r' has left the network");
            CloudPlayer cloudPlayer = (CloudPlayer) new ConfigDriver().convert(new RestDriver(config.getManagerAddress(), config.getRestApiCommunication()).get("/cloudplayer~" + ((PackageCloudPlayerChangeService) paramPacket).getName()), CloudPlayer.class);
            CloudManager.serviceDriver.getService(cloudPlayer.getCurrentServer()).handelCloudPlayerConnection(false);
            CloudManager.serviceDriver.getService(cloudPlayer.getCurrentProxy()).handelCloudPlayerConnection(false);
            Driver.getInstance().getWebServer().removeRoute("/cloudplayer~" + ((PackageCloudPlayerDisconnect) paramPacket).getName());
        Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerDisconnectEvent(((PackageCloudPlayerDisconnect) paramPacket).getName(), ((PackageCloudPlayerDisconnect) paramPacket).getUuid()));
        NettyDriver.getInstance().nettyServer.sendToAllPackets(paramPacket);
    }
        if (paramPacket instanceof PackageCloudPlayerChangeService){
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            CloudPlayer cloudPlayer = (CloudPlayer) new ConfigDriver().convert(new RestDriver(config.getManagerAddress(), config.getRestApiCommunication()).get("/cloudplayer~" + ((PackageCloudPlayerChangeService) paramPacket).getName()), CloudPlayer.class);
            if (cloudPlayer == null) return;
            if (disconnected.contains(((PackageCloudPlayerChangeService) paramPacket).getName())){
                disconnected.remove(((PackageCloudPlayerChangeService) paramPacket).getName());
            }else {
                CloudManager.serviceDriver.getService(cloudPlayer.getCurrentServer()).handelCloudPlayerConnection(false);
                CloudManager.serviceDriver.getService(((PackageCloudPlayerChangeService) paramPacket).getServer()).handelCloudPlayerConnection(true);
                Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerSwitchServiceEvent(paramChannelHandlerContext.name(), cloudPlayer.getCurrentServer(), ((PackageCloudPlayerChangeService) paramPacket).getServer()));
                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '§f"+((PackageCloudPlayerChangeService) paramPacket).getName()+"@"+UUID.randomUUID()+"§r' ist jetzt auf dem Server '§f"+((PackageCloudPlayerChangeService) paramPacket).getServer()+"§r'",
                        "the player '§f"+((PackageCloudPlayerChangeService) paramPacket).getName()+"@"+UUID.randomUUID()+"§r' is now on the server '§"+((PackageCloudPlayerChangeService) paramPacket).getServer()+"§r'");
                cloudPlayer.setCurrentServer(((PackageCloudPlayerChangeService) paramPacket).getServer());
                Driver.getInstance().getWebServer().updateRoute("cloudplayer~" + ((PackageCloudPlayerChangeService) paramPacket).getName(), new ConfigDriver().convert(cloudPlayer));
                NettyDriver.getInstance().nettyServer.sendToAllPackets(paramPacket);
            }
        }
    }

    @Override
    public void onConnect(ChannelHandlerContext paramChannelHandlerContext) {

    }

    @Override
    public void onDisconnect(ChannelHandlerContext paramChannelHandlerContext) {

    }
}
