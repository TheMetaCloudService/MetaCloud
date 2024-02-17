package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.player.CloudPlayerDisconnectedEvent;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.PacketOutPlayerDisconnect;
import eu.metacloudservice.storage.UUIDDriver;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;

public class HandlePacketOutPlayerDisconnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutPlayerDisconnect){
            if (CloudAPI.getInstance().getPlayerPool().playerIsNotNull(((PacketOutPlayerDisconnect) packet).getName())){
                CloudAPI.getInstance().getPlayerPool().unregisterPlayer(((PacketOutPlayerDisconnect) packet).getName());
                CloudAPI.getInstance().getAsyncPlayerPool().unregisterPlayer(((PacketOutPlayerDisconnect) packet).getName());
                CloudAPI.getInstance().getEventDriver().executeEvent(new CloudPlayerDisconnectedEvent(((PacketOutPlayerDisconnect) packet).getName(), UUIDDriver.getUUID(((PacketOutPlayerDisconnect) packet).getName())));
            }
        }
    }
}
