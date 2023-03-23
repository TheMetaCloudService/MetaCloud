package eu.metacloudservice.networking.out.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutServiceReaction extends Packet {

    public PacketOutServiceReaction() {
        setPacketUUID(902309490);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {

    }

    @Override
    public void writePacket(NettyBuffer buffer) {

    }
}
