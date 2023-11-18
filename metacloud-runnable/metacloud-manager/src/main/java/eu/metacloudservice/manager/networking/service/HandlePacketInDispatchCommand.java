package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInDispatchCommand;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInDispatchCommand implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof  PacketInDispatchCommand){
            CloudManager.serviceDriver.getService(((PacketInDispatchCommand) packet).getService()).handelExecute(((PacketInDispatchCommand) packet).getCommand());
        }
    }
}
