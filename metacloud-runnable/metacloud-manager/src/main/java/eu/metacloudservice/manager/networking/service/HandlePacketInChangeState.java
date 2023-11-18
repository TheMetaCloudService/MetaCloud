package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInChangeState;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.process.ServiceState;
import io.netty.channel.Channel;

public class HandlePacketInChangeState implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInChangeState){
            CloudManager.serviceDriver.getService(((PacketInChangeState) packet).getService()).handelStatusChange(ServiceState.valueOf(((PacketInChangeState) packet).getState()));
        }
    }
}
