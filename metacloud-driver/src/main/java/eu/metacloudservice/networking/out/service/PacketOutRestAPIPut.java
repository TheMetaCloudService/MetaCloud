package eu.metacloudservice.networking.out.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutRestAPIPut extends Packet {
    @lombok.Getter
    private String path;
    @lombok.Getter
    private String content;


    public PacketOutRestAPIPut() {
        setPacketUUID(890329912);
    }

    public PacketOutRestAPIPut(String path, String content) {
        setPacketUUID(890329912);
        this.path = path;
        this.content = content;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        this.path = buffer.readString();
        this.content = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(this.path);
        buffer.writeString(this.content);
    }
}
