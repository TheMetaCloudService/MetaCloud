package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.group.CloudGroupDeleteEvent;
import eu.metacloudservice.networking.packet.packets.out.service.group.PacketOutGroupDelete;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketOutGroupDelete implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutGroupDelete){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudGroupDeleteEvent(((PacketOutGroupDelete) packet).getGroup()));
        }
    }
}
