package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.group.CloudGroupCreateEvent;
import eu.metacloudservice.networking.out.service.group.PacketOutGroupCreate;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketOutGroupCreate  implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutGroupCreate){

            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudGroupCreateEvent(((PacketOutGroupCreate) packet).getGroup()));
        }
    }
}
