package eu.metacloudservice.networking.out.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketOutAPIPlayerActionBar extends Packet {

    @Getter
    private String username;
    @Getter
    private String message;

    public PacketOutAPIPlayerActionBar() {
        setPacketUUID(4838291);
    }

    public PacketOutAPIPlayerActionBar(String username, String message) {
        setPacketUUID(4838291);
        this.username = username;
        this.message = message;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        username = buffer.readString();
        message = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(username);
        buffer.writeString(message);
    }
}
