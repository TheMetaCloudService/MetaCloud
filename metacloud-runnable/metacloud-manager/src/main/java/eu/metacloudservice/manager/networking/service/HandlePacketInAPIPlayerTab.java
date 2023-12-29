package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased.PacketInAPIPlayerTab;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerTab;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketInAPIPlayerTab implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInAPIPlayerTab){
            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutAPIPlayerTab(((PacketInAPIPlayerTab) packet).getUsername(), ((PacketInAPIPlayerTab) packet).getHeader(), ((PacketInAPIPlayerTab) packet).getFooter()));
        }
    }
}
