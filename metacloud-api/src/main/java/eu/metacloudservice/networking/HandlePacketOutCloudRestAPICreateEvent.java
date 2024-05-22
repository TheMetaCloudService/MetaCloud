package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPICreateEvent;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudRestAPICreateEvent;
import io.netty.channel.Channel;

public class HandlePacketOutCloudRestAPICreateEvent implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutCloudRestAPICreateEvent){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudRestAPICreateEvent(((PacketOutCloudRestAPICreateEvent) packet).getPath(), ((PacketOutCloudRestAPICreateEvent) packet).getContent()));
        }
    }
}
