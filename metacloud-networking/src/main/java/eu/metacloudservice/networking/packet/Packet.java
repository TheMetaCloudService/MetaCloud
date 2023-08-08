package eu.metacloudservice.networking.packet;

import eu.metacloudservice.networking.packet.NettyBuffer;

public abstract class Packet {

    private int packetUUID;

    public Packet() {
    }

    public int getPacketUUID() {
        return packetUUID;
    }

    public void setPacketUUID(int packetUUID) {
        this.packetUUID = packetUUID;
    }

    public abstract void readPacket(NettyBuffer buffer);

    public abstract void writePacket(NettyBuffer buffer);
}
