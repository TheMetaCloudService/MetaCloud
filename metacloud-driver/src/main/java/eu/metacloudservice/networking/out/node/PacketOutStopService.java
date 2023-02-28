package eu.metacloudservice.networking.out.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketOutStopService extends Packet {

    @Getter
    private String service;

    public PacketOutStopService() {
        setPacketUUID(87432921);
    }

    public PacketOutStopService(String service) {
        setPacketUUID(87432921);
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
