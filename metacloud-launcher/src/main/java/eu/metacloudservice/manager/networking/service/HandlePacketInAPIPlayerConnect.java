package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketInAPIPlayerConnect;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerConnect;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInAPIPlayerConnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInAPIPlayerConnect){
            NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutAPIPlayerConnect(((PacketInAPIPlayerConnect) packet).getUsername(), ((PacketInAPIPlayerConnect) packet).getService()));
        }
    }
}
