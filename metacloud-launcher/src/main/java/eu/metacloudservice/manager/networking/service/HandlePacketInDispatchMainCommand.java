package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.in.service.cloudapi.PacketInDispatchMainCommand;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInDispatchMainCommand implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInDispatchMainCommand){
            Driver.getInstance().getTerminalDriver().getCommandDriver().executeCommand(((PacketInDispatchMainCommand) packet).getCommand());
        }
    }
}
