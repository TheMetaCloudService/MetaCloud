package eu.metacloudservice.networking.in.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInAPIPlayerMessage extends Packet {
    @Getter
    private String username;
    @Getter
    private String message;

    public PacketInAPIPlayerMessage() {
        setPacketUUID(2905493);
    }

    public PacketInAPIPlayerMessage(String username, String message) {
        setPacketUUID(2905493);
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
