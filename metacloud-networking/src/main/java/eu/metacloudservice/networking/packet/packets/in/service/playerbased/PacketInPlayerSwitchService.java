/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.playerbased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInPlayerSwitchService extends Packet {
    @Getter
    private String name;

    @Getter
    private String server;

    public PacketInPlayerSwitchService() {
        setPacketUUID(9829191);
    }

    public PacketInPlayerSwitchService(String name, String server) {
        setPacketUUID(9829191);
        this.name = name;
        this.server = server;
    }

    public void readPacket(@NotNull NettyBuffer buffer) {
        this.name = buffer.readString();
        this.server = buffer.readString();
    }

    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(this.name);
        buffer.writeString(this.server);
    }

}
