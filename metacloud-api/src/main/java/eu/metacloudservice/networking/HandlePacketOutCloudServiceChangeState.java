package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.services.CloudServiceChangeStateEvent;
import eu.metacloudservice.networking.out.service.PacketOutCloudServiceChangeState;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketOutCloudServiceChangeState implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutCloudServiceChangeState){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudServiceChangeStateEvent(((PacketOutCloudServiceChangeState) packet).getName(), ((PacketOutCloudServiceChangeState) packet).getState()));
        }
    }
}
