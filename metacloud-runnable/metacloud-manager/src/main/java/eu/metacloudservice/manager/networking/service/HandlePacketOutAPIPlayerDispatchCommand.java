package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased.PacketOutAPIPlayerDispactchCommand;
import io.netty.channel.Channel;

public class HandlePacketOutAPIPlayerDispatchCommand implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerDispactchCommand){
            NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutAPIPlayerDispactchCommand(((PacketOutAPIPlayerDispactchCommand) packet).getUserName(), ((PacketOutAPIPlayerDispactchCommand) packet).getCommand()));
        }
    }
}
