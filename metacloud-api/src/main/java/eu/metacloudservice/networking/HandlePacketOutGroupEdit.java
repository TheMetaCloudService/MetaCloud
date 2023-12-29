package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.group.CloudGroupUpdateEditEvent;
import eu.metacloudservice.networking.packet.packets.out.service.group.PacketOutGroupDelete;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketOutGroupEdit implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        CloudAPI.getInstance().getEventDriver().executeEvent(new CloudGroupUpdateEditEvent(((PacketOutGroupDelete) packet).getGroup()));
    }
}
