package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased.PacketInCloudPlayerComponent;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutCloudPlayerComponent;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInCloudPlayerComponent implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInCloudPlayerComponent){
            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutCloudPlayerComponent(((PacketInCloudPlayerComponent) packet).getComponent(), ((PacketInCloudPlayerComponent) packet).getPlayer()));
        }
    }
}
