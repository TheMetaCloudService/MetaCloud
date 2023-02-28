package eu.metacloudservice.networking.in.service.playerbased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInPlayerDisconnect extends Packet {

    @Getter
    private String name;

    public PacketInPlayerDisconnect() {
        setPacketUUID(192351);
    }

    public PacketInPlayerDisconnect(String name) {
        setPacketUUID(192351);
        this.name = name;
    }

    public void readPacket(NettyBuffer buffer) {
        this.name = buffer.readString();
    }

    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(this.name);
    }

}
