package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIUpdateEvent;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudRestAPIUpdateEvent;
import io.netty.channel.Channel;

public class HandlePacketOutCloudRestAPIUpdateEvent implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutCloudRestAPIUpdateEvent){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudRestAPIUpdateEvent(((PacketOutCloudRestAPIUpdateEvent) packet).getPath(), ((PacketOutCloudRestAPIUpdateEvent) packet).getContent()));
        }
    }
}
