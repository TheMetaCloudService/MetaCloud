package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased.PacketInAPIPlayerTitle;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerTitle;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInAPIPlayerTitle implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInAPIPlayerTitle){
            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutAPIPlayerTitle(((PacketInAPIPlayerTitle) packet).getTitle(), ((PacketInAPIPlayerTitle) packet).getSubTitle(), ((PacketInAPIPlayerTitle) packet).getFadeIn(), ((PacketInAPIPlayerTitle) packet).getStay(), ((PacketInAPIPlayerTitle) packet).getFadeOut(), ((PacketInAPIPlayerTitle) packet).getUsername()));
        }
    }
}
