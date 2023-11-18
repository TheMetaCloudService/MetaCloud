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

public class PacketInCommandWhitelist extends Packet {
    @Getter
    private String name;


    public PacketInCommandWhitelist() {
        setPacketUUID(890349800);
    }

    public PacketInCommandWhitelist(String name) {
        setPacketUUID(890349800);
        this.name = name;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
            this.name = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(this.name);
    }
}
