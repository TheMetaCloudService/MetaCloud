package eu.themetacloudservice.manager.networking;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
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
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

public class ManageCloudPlayerChannel implements IPacketListener {


    private ArrayList<String> disconnected;

    public ManageCloudPlayerChannel() {
        disconnected = new ArrayList<>();
    }

    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {


        if (paramPacket instanceof PackageCloudPlayerConnect){
            if (CloudManager.shutdown){
                return;
            }

            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '§f"+((PackageCloudPlayerConnect) paramPacket).getName()+"@"+((PackageCloudPlayerConnect) paramPacket).getUuid()+"§r' ist mit dem Proxy '§f"+((PackageCloudPlayerConnect) paramPacket).getProxy()+"§r' verbunden",
                    "The player '§f"+((PackageCloudPlayerConnect) paramPacket).getName()+"@"+((PackageCloudPlayerConnect) paramPacket).getUuid()+"§r' is connected to the proxy '§f"+((PackageCloudPlayerConnect) paramPacket).getProxy()+"§r'");


            CloudManager.serviceDriver.getService(((PackageCloudPlayerConnect) paramPacket).getProxy()).handelCloudPlayerConnection(true);
             NettyDriver.getInstance().nettyServer.sendToAllPackets(paramPacket);
             Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerConnectEvent(((PackageCloudPlayerConnect) paramPacket).getName(), ((PackageCloudPlayerConnect) paramPacket).getProxy(), ((PackageCloudPlayerConnect) paramPacket).getUuid()));
        }

        if (paramPacket instanceof PackageCloudPlayerDisconnect){
            if (CloudManager.shutdown){
                return;
            }

            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '§f"+((PackageCloudPlayerDisconnect) paramPacket).getName()+"@"+((PackageCloudPlayerDisconnect) paramPacket).getUuid()+"§r' hat das Netzwerk verlassen", "The player '§f"+((PackageCloudPlayerDisconnect) paramPacket).getName()+"@"+((PackageCloudPlayerDisconnect) paramPacket).getUuid()+"§r' has left the network");
        Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerDisconnectEvent(((PackageCloudPlayerDisconnect) paramPacket).getName(), ((PackageCloudPlayerDisconnect) paramPacket).getUuid()));
        NettyDriver.getInstance().nettyServer.sendToAllPackets(paramPacket);
    }
        if (paramPacket instanceof PackageCloudPlayerChangeService){
            if (CloudManager.shutdown){
                return;
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
