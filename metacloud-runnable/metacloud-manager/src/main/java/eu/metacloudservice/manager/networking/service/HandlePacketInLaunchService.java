package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInLaunchService;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;

public class HandlePacketInLaunchService implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInLaunchService){
            Driver.getInstance().getTerminalDriver().getCommandDriver().executeCommand("service run " + ((PacketInLaunchService) packet).getGroup() + " 1");
        }
    }
}
