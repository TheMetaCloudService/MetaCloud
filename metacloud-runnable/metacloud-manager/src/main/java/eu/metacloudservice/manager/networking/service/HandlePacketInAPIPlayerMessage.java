package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased.PacketInAPIPlayerMessage;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerMessage;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInAPIPlayerMessage implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInAPIPlayerMessage){
            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutAPIPlayerMessage(((PacketInAPIPlayerMessage) packet).getUsername(), ((PacketInAPIPlayerMessage) packet).getMessage()));
        }
    }
}
