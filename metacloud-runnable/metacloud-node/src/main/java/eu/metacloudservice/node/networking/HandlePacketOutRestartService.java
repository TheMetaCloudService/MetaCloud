package eu.metacloudservice.node.networking;

import eu.metacloudservice.networking.packet.packets.out.node.PacketOutRestartService;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.node.CloudNode;
import io.netty.channel.Channel;

public class HandlePacketOutRestartService implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutRestartService){
            CloudNode.cloudServiceDriver.handleRestart(((PacketOutRestartService) packet).getService());
        }
    }
}
