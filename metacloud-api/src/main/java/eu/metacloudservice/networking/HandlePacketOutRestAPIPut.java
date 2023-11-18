package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIPutEvent;
import eu.metacloudservice.networking.packet.packets.out.service.PacketOutRestAPIPut;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketOutRestAPIPut implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutRestAPIPut){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudRestAPIPutEvent(((PacketOutRestAPIPut) packet).getPath(), ((PacketOutRestAPIPut) packet).getContent()));
        }
    }
}
