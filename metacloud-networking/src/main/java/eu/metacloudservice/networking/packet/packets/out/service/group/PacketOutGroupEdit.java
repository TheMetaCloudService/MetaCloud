/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service.group;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import org.jetbrains.annotations.NotNull;

public class PacketOutGroupEdit extends Packet {
    private String group;


    public PacketOutGroupEdit() {
        setPacketUUID(43212312);
    }

    public PacketOutGroupEdit(String group) {
        setPacketUUID(43212312);
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

    public String getGroup() {
        return group;
    }

}
