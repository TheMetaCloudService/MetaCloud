/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInShutdownNode extends Packet {

    @Getter
    private String node;
    public PacketInShutdownNode() {
        setPacketUUID(87435923);
    }

    public PacketInShutdownNode(String node) {
        setPacketUUID(87435923);
        this.node = node;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.node = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(this.node);
    }
}
