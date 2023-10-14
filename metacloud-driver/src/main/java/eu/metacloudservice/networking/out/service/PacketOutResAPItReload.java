/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.out.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutResAPItReload extends Packet {

    public PacketOutResAPItReload() {
        setPacketUUID(908439023);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
    }
}
