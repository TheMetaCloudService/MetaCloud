package eu.metacloudservice.networking.in.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInServiceReaction extends Packet {

    @Getter
    private String  service;


    public PacketInServiceReaction() {
        setPacketUUID(2209492);
    }

    public PacketInServiceReaction(String service) {
        setPacketUUID(2209492);
        this.service = service;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        service = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
            buffer.writeString(service);
    }
}
