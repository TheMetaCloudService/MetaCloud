package eu.metacloudservice.networking;

import eu.metacloudservice.networking.out.service.group.PacketOutGroupDelete;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketOutGroupDelete implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutGroupDelete){

        }
    }
}
