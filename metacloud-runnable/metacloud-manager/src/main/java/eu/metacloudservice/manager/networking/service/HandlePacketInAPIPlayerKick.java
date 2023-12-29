package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased.PacketInAPIPlayerKick;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerKick;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketInAPIPlayerKick implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {

        if (packet instanceof PacketInAPIPlayerKick){
            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutAPIPlayerKick(((PacketInAPIPlayerKick) packet).getUsername(), ((PacketInAPIPlayerKick) packet).getMessage()));
        }

    }


}
