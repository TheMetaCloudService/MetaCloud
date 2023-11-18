package eu.metacloudservice.node.networking;

import eu.metacloudservice.networking.packet.packets.out.node.PacketOutSendCommand;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.node.CloudNode;
import io.netty.channel.Channel;

public class HandlePacketOutSendCommand implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutSendCommand){
            CloudNode.cloudServiceDriver.execute(((PacketOutSendCommand)packet).getService(), ((PacketOutSendCommand)packet).getCommand());
        }
    }
}
