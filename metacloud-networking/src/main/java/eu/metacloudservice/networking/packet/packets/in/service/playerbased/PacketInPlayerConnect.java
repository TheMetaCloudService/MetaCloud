/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.playerbased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInPlayerConnect extends Packet {

    @Getter
    private String name;

    @Getter
    private String proxy;

    public PacketInPlayerConnect() {
        setPacketUUID(2995985);
    }

    public PacketInPlayerConnect(String name, String proxy) {
        setPacketUUID(2995985);
        this.name = name;
        this.proxy = proxy;
    }

    public void readPacket(@NotNull NettyBuffer buffer) {
        this.name = buffer.readString();
        this.proxy = buffer.readString();
    }

    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(this.name);
        buffer.writeString(this.proxy);
    }

}
