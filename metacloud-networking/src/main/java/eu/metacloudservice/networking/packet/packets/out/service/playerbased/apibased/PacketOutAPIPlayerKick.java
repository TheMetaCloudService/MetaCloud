/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutAPIPlayerKick extends Packet {

    @Getter
    private String username;
    @Getter
    private String message;

    public PacketOutAPIPlayerKick() {
        setPacketUUID(829341212);
    }

    public PacketOutAPIPlayerKick(String username, String message) {
        setPacketUUID(829341212);
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
