package eu.metacloudservice.networking.out.service.playerbased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketOutPlayerConnect extends Packet {

    @Getter
    private String name;

    public PacketOutPlayerConnect() {
        setPacketUUID(423293);
    }

    public PacketOutPlayerConnect(String name) {
        setPacketUUID(423293);
        this.name = name;
    }

    public void readPacket(NettyBuffer buffer) {
        this.name = buffer.readString();
    }

    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(this.name);
    }

}
