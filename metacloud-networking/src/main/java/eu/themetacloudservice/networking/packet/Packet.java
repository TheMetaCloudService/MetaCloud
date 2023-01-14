package eu.themetacloudservice.networking.packet;

import eu.themetacloudservice.networking.packet.enums.PacketSender;

public abstract class Packet {

    private int packetUUID;

    public Packet(){
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
