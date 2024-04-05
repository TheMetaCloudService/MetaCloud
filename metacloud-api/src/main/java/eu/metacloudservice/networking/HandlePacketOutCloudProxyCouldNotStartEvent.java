package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.services.CloudProxyCouldNotStartEvent;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudProxyCouldNotStartEvent;
import io.netty.channel.Channel;

public class HandlePacketOutCloudProxyCouldNotStartEvent implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutCloudProxyCouldNotStartEvent){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudProxyCouldNotStartEvent(((PacketOutCloudProxyCouldNotStartEvent) packet).getName()));
        }
    }
}
