package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.player.async.entrys.AsyncCloudPlayer;
import eu.metacloudservice.events.listeners.player.CloudPlayerConnectedEvent;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.PacketOutPlayerConnect;
import eu.metacloudservice.player.entrys.CloudPlayer;
import eu.metacloudservice.storage.UUIDDriver;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;

import java.util.UUID;

public class HandlePacketOutPlayerConnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutPlayerConnect){
            if (!CloudAPI.getInstance().getPlayerPool().playerIsNotNull(((PacketOutPlayerConnect) packet).getName())){

                UUID uniqueId = UUIDDriver.getUUID(((PacketOutPlayerConnect) packet).getName());

                CloudAPI.getInstance().getAsyncPlayerPool().registerPlayer(new AsyncCloudPlayer(((PacketOutPlayerConnect) packet).getName(),
                        uniqueId));

                CloudAPI.getInstance().getPlayerPool().registerPlayer(new CloudPlayer(((PacketOutPlayerConnect) packet).getName(),
                        uniqueId));


                CloudAPI.getInstance().getEventDriver().executeEvent(new CloudPlayerConnectedEvent(((PacketOutPlayerConnect) packet).getName(),
                        ((PacketOutPlayerConnect) packet).getProxy(), UUIDDriver.getUUID(((PacketOutPlayerConnect) packet).getName())));
            }

         }
    }
}
