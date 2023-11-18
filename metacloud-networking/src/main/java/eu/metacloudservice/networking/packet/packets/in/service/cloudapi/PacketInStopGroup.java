/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.cloudapi;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInStopGroup extends Packet {

    @Getter
    private String group;

    public PacketInStopGroup() {
        setPacketUUID(821321);
    }

    public PacketInStopGroup(String group) {
        setPacketUUID(821321);
        this.group = group;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        group = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(group);
    }
}
