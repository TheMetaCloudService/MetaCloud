/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketOutSendCommandToNodeConsole extends Packet {

    @Getter
    private String command;


    public PacketOutSendCommandToNodeConsole() {
        setPacketUUID(4399293);
    }

    public PacketOutSendCommandToNodeConsole(String command) {
        setPacketUUID(4399293);
        this.command = command;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        this.command = buffer.readString();

    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(this.command);
    }
}
