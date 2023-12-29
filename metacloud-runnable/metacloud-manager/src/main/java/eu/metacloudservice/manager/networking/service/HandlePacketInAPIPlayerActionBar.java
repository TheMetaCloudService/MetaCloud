package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased.PacketInAPIPlayerActionBar;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerActionBar;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketInAPIPlayerActionBar implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInAPIPlayerActionBar){
            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutAPIPlayerActionBar(((PacketInAPIPlayerActionBar) packet).getUsername(), ((PacketInAPIPlayerActionBar) packet).getMessage()));
        }
    }
}
