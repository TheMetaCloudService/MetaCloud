package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIDeleteEvent;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudRestAPIDeleteEvent;
import io.netty.channel.Channel;

public class HandlePacketOutCloudRestAPIDeleteEvent implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutCloudRestAPIDeleteEvent){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudRestAPIDeleteEvent(((PacketOutCloudRestAPIDeleteEvent) packet).getPath()));
        }
    }
}
