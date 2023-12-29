package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInStopGroup;
import io.netty.channel.Channel;

public class HandlePacketInStopGroup implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInStopGroup){
            Driver.getInstance().getTerminalDriver().getCommandDriver().executeCommand("service stopgroup " + ((PacketInStopGroup) packet).getGroup());
        }
    }
}
