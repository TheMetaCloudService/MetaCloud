package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInDispatchMainCommand;
import io.netty.channel.Channel;import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;

public class HandlePacketInDispatchMainCommand implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInDispatchMainCommand){
            Driver.getInstance().getTerminalDriver().getCommandDriver().executeCommand(((PacketInDispatchMainCommand) packet).getCommand());
        }
    }
}
