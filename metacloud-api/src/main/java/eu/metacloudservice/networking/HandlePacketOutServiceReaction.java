package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.networking.in.service.PacketInServiceReaction;
import eu.metacloudservice.networking.out.service.PacketOutServiceReaction;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketOutServiceReaction implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutServiceReaction){
            NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInServiceReaction(CloudAPI.getInstance().getCurrentService().getService()));
        }
    }
}
