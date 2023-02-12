package eu.themetacloudservice.manager.networking;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.cloudplayer.CloudPlayerRestCech;
import eu.themetacloudservice.events.dummys.cloudplayerbased.CloudPlayerConnectEvent;
import eu.themetacloudservice.events.dummys.cloudplayerbased.CloudPlayerDisconnectEvent;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerChangeService;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerConnect;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerDisconnect;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.webserver.RestDriver;
import eu.themetacloudservice.webserver.entry.RouteEntry;
import io.netty.channel.ChannelHandlerContext;

public class ManageCloudPlayerChannel implements IPacketListener {



    public ManageCloudPlayerChannel() {
    }

    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {


        if (paramPacket instanceof PackageCloudPlayerConnect){
            if (!CloudManager.shutdown){
                CloudPlayerRestCech restCech = new CloudPlayerRestCech(paramChannelHandlerContext.name(),  ((PackageCloudPlayerConnect) paramPacket).getUuid());
                restCech.handleConnect(((PackageCloudPlayerConnect) paramPacket).getProxy());
                Driver.getInstance().getWebServer().addRoute( new RouteEntry("CLOUDPLAYER~" + ((PackageCloudPlayerConnect) paramPacket).getName(), new RestDriver().convert(restCech)));


                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '§f"+((PackageCloudPlayerConnect) paramPacket).getName()+"@"+((PackageCloudPlayerConnect) paramPacket).getUuid()+"§r' ist mit dem Proxy '§f"+((PackageCloudPlayerConnect) paramPacket).getProxy()+"§r' verbunden",
                        "The player '§f"+((PackageCloudPlayerConnect) paramPacket).getName()+"@"+((PackageCloudPlayerConnect) paramPacket).getUuid()+"§r' is connected to the proxy '§f"+((PackageCloudPlayerConnect) paramPacket).getProxy()+"§r'");


                CloudManager.serviceDriver.getService(((PackageCloudPlayerConnect) paramPacket).getProxy()).handelCloudPlayerConnection(true);
                NettyDriver.getInstance().nettyServer.sendToAllPackets(paramPacket);
                Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerConnectEvent(((PackageCloudPlayerConnect) paramPacket).getName(), ((PackageCloudPlayerConnect) paramPacket).getProxy(), ((PackageCloudPlayerConnect) paramPacket).getUuid()));

            }
        }

        if (paramPacket instanceof PackageCloudPlayerDisconnect){
            if (!CloudManager.shutdown){
                Driver.getInstance().getWebServer().removeRoute("CLOUDPLAYER~" + ((PackageCloudPlayerDisconnect) paramPacket).getName());

                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '§f"+((PackageCloudPlayerDisconnect) paramPacket).getName()+"@"+((PackageCloudPlayerDisconnect) paramPacket).getUuid()+"§r' hat das Netzwerk verlassen", "The player '§f"+((PackageCloudPlayerDisconnect) paramPacket).getName()+"@"+((PackageCloudPlayerDisconnect) paramPacket).getUuid()+"§r' has left the network");
                Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerDisconnectEvent(((PackageCloudPlayerDisconnect) paramPacket).getName(), ((PackageCloudPlayerDisconnect) paramPacket).getUuid()));
                NettyDriver.getInstance().nettyServer.sendToAllPackets(paramPacket);
            }
    }
        if (paramPacket instanceof PackageCloudPlayerChangeService){
            if (!CloudManager.shutdown){
                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '§f"+((PackageCloudPlayerChangeService) paramPacket).getName()+"§r' ist den Server gejoint '§f"+((PackageCloudPlayerChangeService) paramPacket).getServer()+"§r'",
                        "The player '§f"+((PackageCloudPlayerChangeService) paramPacket).getName()+"§r' has joined the server '§f"+((PackageCloudPlayerChangeService) paramPacket).getServer()+"§r'");

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
