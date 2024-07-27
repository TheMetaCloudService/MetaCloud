/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInAPIPlayerKick extends Packet {
    @Getter
    private String username;
    @Getter
    private String message;

    public PacketInAPIPlayerKick() {
        setPacketUUID(233123);
    }

    public PacketInAPIPlayerKick(String username, String message) {
        setPacketUUID(233123);
        this.username = username;
        this.message = message;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.username = buffer.readString();
        this.message = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(username);
        buffer.writeString(message);
    }
}
