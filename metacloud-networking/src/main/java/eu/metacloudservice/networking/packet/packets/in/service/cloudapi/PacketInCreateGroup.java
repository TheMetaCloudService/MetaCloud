/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.cloudapi;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInCreateGroup extends Packet {

    @Getter
    private String groupConfig;

    public PacketInCreateGroup() {
        setPacketUUID(398293);
    }

    public PacketInCreateGroup(String groupConfig) {
        setPacketUUID(398293);
        this.groupConfig = groupConfig;
    }


    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        groupConfig = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(groupConfig);
    }
}
