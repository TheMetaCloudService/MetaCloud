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

public class PacketInCommandMaintenance extends Packet {
    @Getter
    private String name;
    @Getter
    private boolean removed;


    public PacketInCommandMaintenance() {
        setPacketUUID(382145654);
    }

    public PacketInCommandMaintenance(String name, boolean removed) {
        setPacketUUID(382145654);
        this.name = name;
        this.removed = removed;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.name = buffer.readString();
        this.removed = buffer.readBoolean();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(this.name);
        buffer.writeBoolean(this.removed);
    }
}
