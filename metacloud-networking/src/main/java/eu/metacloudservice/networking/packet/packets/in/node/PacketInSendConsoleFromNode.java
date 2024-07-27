/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInSendConsoleFromNode extends Packet {

    @Getter
    private String line;

    public PacketInSendConsoleFromNode() {
        setPacketUUID(29123812);
    }

    public PacketInSendConsoleFromNode(String line) {
        setPacketUUID(29123812);
        this.line = line;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.line = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(this.line);
    }
}
