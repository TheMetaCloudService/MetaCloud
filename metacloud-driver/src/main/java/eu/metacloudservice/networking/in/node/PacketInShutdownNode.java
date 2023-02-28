package eu.metacloudservice.networking.in.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

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
    public void readPacket(NettyBuffer buffer) {
        this.node = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(this.node);
    }
}
