/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInAuthNode extends Packet {

    @Getter
    private String node;
    @Getter
    private String key;

    public PacketInAuthNode() {
        setPacketUUID(298234);
    }

    public PacketInAuthNode(String node, String key) {
        setPacketUUID(298234);
        this.node = node;
        this.key = key;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.node = buffer.readString();
        this.key = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(node);
        buffer.writeString(key);
    }

}
