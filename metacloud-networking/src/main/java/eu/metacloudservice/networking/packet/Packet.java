package eu.metacloudservice.networking.packet;

import org.jetbrains.annotations.NotNull;

public abstract class Packet {

    private int packetUUID;

    public Packet() {
    }

    public int getPacketUUID() {
        return packetUUID;
    }

    public void setPacketUUID(final int packetUUID) {
        this.packetUUID = packetUUID;
    }

    public abstract void readPacket(@NotNull NettyBuffer buffer);

    public abstract void writePacket(@NotNull NettyBuffer buffer);
}
