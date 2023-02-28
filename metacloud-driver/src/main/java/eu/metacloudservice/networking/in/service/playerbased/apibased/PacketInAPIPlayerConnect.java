package eu.metacloudservice.networking.in.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInAPIPlayerConnect extends Packet {

    @Getter
    private String username;
    @Getter
    private String service;

    public PacketInAPIPlayerConnect() {
        setPacketUUID(100230);
    }

    public PacketInAPIPlayerConnect(String username, String service) {
        setPacketUUID(100230);
        this.username = username;
        this.service = service;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        this.username = buffer.readString();
        this.service = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(username);
        buffer.writeString(service);
    }
}
