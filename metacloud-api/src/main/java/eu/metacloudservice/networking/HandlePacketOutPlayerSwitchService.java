package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.events.listeners.CloudPlayerSwitchEvent;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerSwitchService;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.storage.UUIDDriver;
import io.netty.channel.Channel;

public class HandlePacketOutPlayerSwitchService implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutPlayerSwitchService){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudPlayerSwitchEvent(((PacketOutPlayerSwitchService) packet).getName(), UUIDDriver.getUUID(((PacketOutPlayerSwitchService) packet).getName()), ((PacketOutPlayerSwitchService) packet).getFrom(), ((PacketOutPlayerSwitchService) packet).getServer()));
        }
    }
}
