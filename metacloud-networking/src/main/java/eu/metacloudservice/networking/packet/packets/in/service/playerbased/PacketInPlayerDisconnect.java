/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.playerbased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInPlayerDisconnect extends Packet {

    @Getter
    private String name;

    public PacketInPlayerDisconnect() {
        setPacketUUID(192351);
    }

    public PacketInPlayerDisconnect(String name) {
        setPacketUUID(192351);
        this.name = name;
    }

    public void readPacket(@NotNull NettyBuffer buffer) {
        this.name = buffer.readString();
    }

    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(this.name);
    }

}
