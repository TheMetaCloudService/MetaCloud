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

public class PacketInCommandMaxPlayers extends Packet {

    @Getter
    private String group;
    @Getter
    private Integer amount;

    public PacketInCommandMaxPlayers(String group, Integer amount) {
        setPacketUUID(918439129);
        this.group = group;
        this.amount = amount;
    }

    public PacketInCommandMaxPlayers() {
        setPacketUUID(918439129);
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
