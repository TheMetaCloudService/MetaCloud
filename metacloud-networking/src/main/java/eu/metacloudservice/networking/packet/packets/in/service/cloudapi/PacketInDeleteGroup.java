/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.cloudapi;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInDeleteGroup extends Packet {
    @Getter
    private String group;

    public PacketInDeleteGroup() {
        setPacketUUID(98034934);
    }


    public PacketInDeleteGroup(String group) {
        setPacketUUID(98034934);
        this.group = group;
    }


    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        group = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(group);
    }
}
