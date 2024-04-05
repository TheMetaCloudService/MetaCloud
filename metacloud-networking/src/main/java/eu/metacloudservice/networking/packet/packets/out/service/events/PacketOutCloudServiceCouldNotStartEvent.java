package eu.metacloudservice.networking.packet.packets.out.service.events;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutCloudServiceCouldNotStartEvent extends Packet {

    @Getter
    private String name;


    public PacketOutCloudServiceCouldNotStartEvent() {
        setPacketUUID(431432331);
    }

    public PacketOutCloudServiceCouldNotStartEvent(String name) {
        setPacketUUID(431432331);
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
