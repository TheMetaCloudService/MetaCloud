/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutSendCommand extends Packet {

    @Getter
    private String command;
    @Getter
    private String service;

    public PacketOutSendCommand() {
        setPacketUUID(59488922);
    }

    public PacketOutSendCommand(String command, String service) {
        setPacketUUID(59488922);
        this.command = command;
        this.service = service;
    }
    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.command = buffer.readString();
        this.service = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(this.command);
        buffer.writeString(this.service);
    }
}
