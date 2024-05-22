package eu.metacloudservice.networking.packet.packets.out.service.events;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import org.jetbrains.annotations.NotNull;

public class PacketOutCloudRestAPIReloadEvent extends Packet {

    public PacketOutCloudRestAPIReloadEvent() {
        setPacketUUID(929123137);
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {

    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {

    }
}
