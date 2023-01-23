package eu.themetacloudservice.node.networking;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceExit;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceLaunch;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceExit;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceLaunch;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelSync;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.node.CloudNode;
import eu.themetacloudservice.process.ServiceProcess;
import eu.themetacloudservice.terminal.enums.Type;
import io.netty.channel.ChannelHandlerContext;

public class NodeHandelServicesChannel implements IPacketListener {


    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {

        if (paramPacket instanceof PackageToNodeHandelServiceLaunch){
            PackageToNodeHandelServiceLaunch packet = (PackageToNodeHandelServiceLaunch)paramPacket;
            Driver.getInstance().getTerminalDriver().log(Type.DEBUG, "start: " + packet.getService());
            ServiceProcess process = CloudNode.serviceDriver.handelLaunch(packet.getService(),
                    packet.isUseProtocol());
            PackageToManagerCallBackServiceLaunch callBack = new PackageToManagerCallBackServiceLaunch(packet.getService(), "Node-1", 5000);
            paramChannelHandlerContext.channel().writeAndFlush(callBack);
            return;
        } if (paramPacket instanceof PackageToNodeHandelServiceExit){
            PackageToNodeHandelServiceExit packet = (PackageToNodeHandelServiceExit)paramPacket;
            CloudNode.serviceDriver.handelQuit(packet.getService());
            Driver.getInstance().getTerminalDriver().log(Type.NETWORK, "debug -> stop: " + packet.getService());
            PackageToManagerCallBackServiceExit callBack = new PackageToManagerCallBackServiceExit(packet.getService());
            paramChannelHandlerContext.channel().writeAndFlush(callBack);
            return;
        } if (paramPacket instanceof PackageToNodeHandelSync){
            PackageToNodeHandelSync packet = (PackageToNodeHandelSync)paramPacket;
            Driver.getInstance().getTerminalDriver().log(Type.NETWORK, "debug -> sync: " + packet.getService());
            CloudNode.serviceDriver.handelSync(packet.getService());
            return;
        }
    }

    @Override
    public void onConnect(ChannelHandlerContext paramChannelHandlerContext) {

    }

    @Override
    public void onDisconnect(ChannelHandlerContext paramChannelHandlerContext) {

    }
}
