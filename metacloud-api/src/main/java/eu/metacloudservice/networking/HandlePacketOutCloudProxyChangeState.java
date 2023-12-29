package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.services.CloudProxyChangeStateEvent;
import eu.metacloudservice.networking.packet.packets.out.service.PacketOutCloudProxyChangeState;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketOutCloudProxyChangeState implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutCloudProxyChangeState){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudProxyChangeStateEvent(((PacketOutCloudProxyChangeState) packet).getName(), ((PacketOutCloudProxyChangeState) packet).getState()));
        }
    }
}
