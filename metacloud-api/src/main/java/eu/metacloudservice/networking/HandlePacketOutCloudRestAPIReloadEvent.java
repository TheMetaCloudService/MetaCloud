package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.entrys.IEventAdapter;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIReloadEvent;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudRestAPIReloadEvent;
import io.netty.channel.Channel;

public class HandlePacketOutCloudRestAPIReloadEvent implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutCloudRestAPIReloadEvent){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudRestAPIReloadEvent());
        }
    }
}
