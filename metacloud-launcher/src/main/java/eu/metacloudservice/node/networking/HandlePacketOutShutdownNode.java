package eu.metacloudservice.node.networking;

import eu.metacloudservice.networking.out.node.PacketOutShutdownNode;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketOutShutdownNode implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutShutdownNode){
            System.exit(0);
        }
    }
}
