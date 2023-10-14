package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.group.CloudGroupDeleteEvent;
import eu.metacloudservice.events.listeners.group.CloudGroupUpdateEditEvent;
import eu.metacloudservice.networking.out.service.group.PacketOutGroupDelete;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketOutGroupEdit implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        CloudAPI.getInstance().getEventDriver().executeEvent(new CloudGroupUpdateEditEvent(((PacketOutGroupDelete) packet).getGroup()));
    }
}
