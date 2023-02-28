package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketInAPIPlayerKick;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerKick;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInAPIPlayerKick implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {

        if (packet instanceof PacketInAPIPlayerKick){
            NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutAPIPlayerKick(((PacketInAPIPlayerKick) packet).getUsername(), ((PacketInAPIPlayerKick) packet).getMessage()));
        }

    }
}
