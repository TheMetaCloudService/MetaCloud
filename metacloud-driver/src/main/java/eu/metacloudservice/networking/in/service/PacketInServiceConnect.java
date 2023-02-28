package eu.metacloudservice.networking.in.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInServiceConnect extends Packet {

    @Getter
    private String service;

    public PacketInServiceConnect() {
        setPacketUUID(239945);
    }

    public PacketInServiceConnect(String service) {
        setPacketUUID(239945);
        this.service = service;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        this.service = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(this.service);
    }
}
