package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.events.listeners.CloudPlayerDisconnectedEvent;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerDisconnect;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.storage.UUIDDriver;
import io.netty.channel.Channel;

public class HandlePacketOutPlayerDisconnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutPlayerDisconnect){
            CloudAPI.getInstance().getPlayerPool().unregisterPlayer(UUIDDriver.getUUID(((PacketOutPlayerDisconnect) packet).getName()));
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudPlayerDisconnectedEvent(((PacketOutPlayerDisconnect) packet).getName(), UUIDDriver.getUUID(((PacketOutPlayerDisconnect) packet).getName())));
        }
    }
}
