/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.command;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

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
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.group = buffer.readString();
        this.amount = buffer.readInt();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(this.group);
        buffer.writeInt(this.amount);
    }


}
