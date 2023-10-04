/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.in.service.command;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInCommandMinCount extends Packet {

    @Getter
    private String group;
    @Getter
    private Integer amount;

    public PacketInCommandMinCount(String group, Integer amount) {
        setPacketUUID(4953982);
        this.group = group;
        this.amount = amount;
    }

    public PacketInCommandMinCount() {
        setPacketUUID(4953982);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        this.group = buffer.readString();
        this.amount = buffer.readInt();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(this.group);
        buffer.writeInt(this.amount);
    }


}
