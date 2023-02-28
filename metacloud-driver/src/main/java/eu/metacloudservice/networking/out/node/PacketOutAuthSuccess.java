package eu.metacloudservice.networking.out.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutAuthSuccess extends Packet {

    public PacketOutAuthSuccess() {
        setPacketUUID(298234);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {}

    @Override
    public void writePacket(NettyBuffer buffer) {}
}
