package eu.metacloudservice.node.networking;

import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.node.PacketOutEnableConsole;
import eu.metacloudservice.node.CloudNode;
import io.netty.channel.Channel;

public class HandlePacketOutEnableConsole implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutEnableConsole){
            CloudNode.cloudServiceDriver.handleConsole(((PacketOutEnableConsole) packet).getService());
        }
    }
}
