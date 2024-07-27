/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.cloudapi;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInDispatchMainCommand extends Packet {

    @Getter
    private String command;

    public PacketInDispatchMainCommand() {
        setPacketUUID(53123);
    }

    public PacketInDispatchMainCommand(String command) {
        setPacketUUID(53123);
        this.command = command;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        command = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(command);
    }
}
