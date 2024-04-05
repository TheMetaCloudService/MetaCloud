package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.services.CloudServiceCouldNotStartEvent;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudServiceCouldNotStartEvent;
import io.netty.channel.Channel;

public class HandlePacketOutCloudServiceCouldNotStartEvent implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutCloudServiceCouldNotStartEvent){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudServiceCouldNotStartEvent(((PacketOutCloudServiceCouldNotStartEvent) packet).getName()));
        }
    }
}
