/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service.playerbased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutPlayerDisconnect extends Packet {

    @Getter
    private String name;

    public PacketOutPlayerDisconnect() {
        setPacketUUID(8643);
    }

    public PacketOutPlayerDisconnect(String name) {
        setPacketUUID(8643);
        this.name = name;
    }

    public void readPacket(@NotNull NettyBuffer buffer) {
        this.name = buffer.readString();
    }

    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(this.name);
    }
}
