package eu.themetacloudservice.node.networking;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceExit;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceLaunch;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceExit;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceLaunch;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelSync;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.node.CloudNode;
import eu.themetacloudservice.node.cloudservices.entry.QueueEntry;
import eu.themetacloudservice.terminal.enums.Type;
import io.netty.channel.ChannelHandlerContext;

public class NodeHandelServicesChannel implements IPacketListener {


    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {

        if (paramPacket instanceof PackageToNodeHandelServiceLaunch){
            PackageToNodeHandelServiceLaunch packet = (PackageToNodeHandelServiceLaunch)paramPacket;
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Neue Aufgabe, den Service '§f"+packet.getService()+"§r' starten", "New task, start the service '§f"+packet.getService()+"§r'");
            CloudNode.cloudServiceDriver.addQueue(new QueueEntry(packet.getService(), packet.getGroup(), packet.isUseProtocol()));
        } if (paramPacket instanceof PackageToNodeHandelServiceExit){
            PackageToNodeHandelServiceExit packet = (PackageToNodeHandelServiceExit)paramPacket;
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Neue Aufgabe, Stoppen des Service '§f"+packet.getService()+"§r'",
                    "New task, Stop the '§f"+packet.getService()+"§r' service");

            CloudNode.cloudServiceDriver.addQueue(new QueueEntry(packet.getService()));
        } if (paramPacket instanceof PackageToNodeHandelSync){
            PackageToNodeHandelSync packet = (PackageToNodeHandelSync)paramPacket;
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Neue Aufgabe, den Service '§f"+packet.getService()+"§r' synchronisieren",
                    "New task, synchronize the '§f"+packet.getService()+"§r' service");
            CloudNode.cloudServiceDriver.sync(packet.getService());
        }
    }

    @Override
    public void onConnect(ChannelHandlerContext paramChannelHandlerContext) {

    }

    @Override
    public void onDisconnect(ChannelHandlerContext paramChannelHandlerContext) {

    }
}
