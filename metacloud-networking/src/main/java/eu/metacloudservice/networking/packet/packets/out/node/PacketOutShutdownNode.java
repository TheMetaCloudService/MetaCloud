/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutShutdownNode extends Packet {

    public PacketOutShutdownNode() {
        setPacketUUID(90329232);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {

    }

    @Override
    public void writePacket(NettyBuffer buffer) {

    }
}
