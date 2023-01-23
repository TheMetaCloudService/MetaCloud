package eu.themetacloudservice.node.networking;

import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelKillNode;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.node.CloudNode;
import io.netty.channel.ChannelHandlerContext;

public class NodeHandelKillChannel implements IPacketListener {
    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {

        if (paramPacket instanceof PackageToNodeHandelKillNode){
            CloudNode.shutdownHook();
        }

    }

    @Override
    public void onConnect(ChannelHandlerContext paramChannelHandlerContext) {

    }

    @Override
    public void onDisconnect(ChannelHandlerContext paramChannelHandlerContext) {

    }
}
