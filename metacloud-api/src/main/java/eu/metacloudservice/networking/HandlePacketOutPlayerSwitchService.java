package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.async.pool.player.entrys.AsyncCloudPlayer;
import eu.metacloudservice.events.listeners.player.CloudPlayerConnectedEvent;
import eu.metacloudservice.events.listeners.player.CloudPlayerSwitchEvent;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.PacketOutPlayerSwitchService;
import eu.metacloudservice.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.storage.UUIDDriver;
import io.netty.channel.Channel;

public class HandlePacketOutPlayerSwitchService implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutPlayerSwitchService){

            if (!CloudAPI.getInstance().getPlayerPool().playerIsNotNull(((PacketOutPlayerSwitchService) packet).getName())){
                AsyncCloudAPI.getInstance().getPlayerPool().registerPlayer(new AsyncCloudPlayer(((PacketOutPlayerSwitchService) packet).getName(), UUIDDriver.getUUID(((PacketOutPlayerSwitchService) packet).getName())));
                CloudAPI.getInstance().getPlayerPool().registerPlayer(new CloudPlayer(((PacketOutPlayerSwitchService) packet).getName(), UUIDDriver.getUUID(((PacketOutPlayerSwitchService) packet).getName())));
                CloudAPI.getInstance().getEventDriver().executeEvent(new CloudPlayerConnectedEvent(((PacketOutPlayerSwitchService) packet).getName(), CloudAPI.getInstance().getPlayerPool().getPlayer(((PacketOutPlayerSwitchService) packet).getName()).getProxyServer().getName(), UUIDDriver.getUUID(((PacketOutPlayerSwitchService) packet).getName())));
            }

            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudPlayerSwitchEvent(((PacketOutPlayerSwitchService) packet).getName(), UUIDDriver.getUUID(((PacketOutPlayerSwitchService) packet).getName()), ((PacketOutPlayerSwitchService) packet).getFrom(), ((PacketOutPlayerSwitchService) packet).getServer()));
        }
    }
}
