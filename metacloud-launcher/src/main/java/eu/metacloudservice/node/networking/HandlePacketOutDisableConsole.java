package eu.metacloudservice.node.networking;

import eu.metacloudservice.networking.out.node.PacketOutDisableConsole;
import eu.metacloudservice.networking.out.node.PacketOutEnableConsole;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.node.CloudNode;
import io.netty.channel.Channel;

public class HandlePacketOutDisableConsole implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutDisableConsole){
            CloudNode.cloudServiceDriver.handleConsole(((PacketOutDisableConsole) packet).getService());
        }
    }
}
