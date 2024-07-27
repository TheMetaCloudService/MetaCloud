/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInAPIPlayerConnect extends Packet {

    @Getter
    private String username;
    @Getter
    private String service;

    public PacketInAPIPlayerConnect() {
        setPacketUUID(10220230);
    }

    public PacketInAPIPlayerConnect(String username, String service) {
        setPacketUUID(10220230);
        this.username = username;
        this.service = service;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.username = buffer.readString();
        this.service = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(username);
        buffer.writeString(service);
    }
}
