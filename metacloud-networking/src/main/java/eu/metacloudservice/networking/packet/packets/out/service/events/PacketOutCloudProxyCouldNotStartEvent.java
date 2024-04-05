package eu.metacloudservice.networking.packet.packets.out.service.events;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutCloudProxyCouldNotStartEvent extends Packet {

    @Getter
    private String name;


    public PacketOutCloudProxyCouldNotStartEvent() {
        setPacketUUID(879342879);
    }

    public PacketOutCloudProxyCouldNotStartEvent(String name) {
        setPacketUUID(879342879);
        this.name = name;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.name = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(this.name);
    }
}
