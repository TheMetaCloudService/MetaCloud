/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutServiceConnected extends Packet {

    @Getter
    private String name;
    @Getter
    private String group;

    public PacketOutServiceConnected() {
        setPacketUUID(288123);
    }

    public PacketOutServiceConnected(String name, String group) {
        setPacketUUID(288123);
        this.name = name;
        this.group = group;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.name = buffer.readString();
        this.group = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(name);
        buffer.writeString(group);
    }
}
