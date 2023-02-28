package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInServiceDisconnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {

    }
}
