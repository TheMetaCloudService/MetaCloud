package eu.metacloudservice.networking.in.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInAPIPlayerActionBar extends Packet {

    @Getter
    private String username;
    @Getter
    private String message;

    public PacketInAPIPlayerActionBar() {
        setPacketUUID(94398439);
    }

    public PacketInAPIPlayerActionBar(String username, String message) {
        setPacketUUID(94398439);
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
