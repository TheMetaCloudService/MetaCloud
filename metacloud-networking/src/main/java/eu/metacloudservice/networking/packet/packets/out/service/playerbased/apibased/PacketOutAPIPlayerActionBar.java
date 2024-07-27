/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutAPIPlayerActionBar extends Packet {

    @Getter
    private String username;
    @Getter
    private String message;

    public PacketOutAPIPlayerActionBar() {
        setPacketUUID(4838291);
    }

    public PacketOutAPIPlayerActionBar(String username, String message) {
        setPacketUUID(4838291);
        this.username = username;
        this.message = message;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        username = buffer.readString();
        message = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(username);
        buffer.writeString(message);
    }
}
