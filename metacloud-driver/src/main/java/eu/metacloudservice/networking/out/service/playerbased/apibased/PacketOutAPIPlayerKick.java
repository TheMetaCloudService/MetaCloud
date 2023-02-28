package eu.metacloudservice.networking.out.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketOutAPIPlayerKick extends Packet {

    @Getter
    private String username;
    @Getter
    private String message;

    public PacketOutAPIPlayerKick() {
        setPacketUUID(829341212);
    }

    public PacketOutAPIPlayerKick(String username, String message) {
        setPacketUUID(829341212);
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
