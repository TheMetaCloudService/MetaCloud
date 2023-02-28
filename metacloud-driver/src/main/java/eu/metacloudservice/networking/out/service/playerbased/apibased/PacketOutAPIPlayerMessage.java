package eu.metacloudservice.networking.out.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketOutAPIPlayerMessage extends Packet {
    @Getter
    private String username;
    @Getter
    private String message;

    public PacketOutAPIPlayerMessage() {
        setPacketUUID(231);
    }

    public PacketOutAPIPlayerMessage(String username, String message) {
        setPacketUUID(231);
        this.username = username;
        this.message = message;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        this.username = buffer.readString();
        this.message = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(username);
        buffer.writeString(message);
    }
}
