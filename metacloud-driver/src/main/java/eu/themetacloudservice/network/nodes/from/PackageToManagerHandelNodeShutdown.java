package eu.themetacloudservice.network.nodes.from;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageToManagerHandelNodeShutdown extends Packet {

    private String node;

    public PackageToManagerHandelNodeShutdown() {
        setPacketUUID(87435923);

    }

    public PackageToManagerHandelNodeShutdown(String node) {
        setPacketUUID(87435923);
        this.node = node;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        node = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(node);
    }

    public String getNode() {
        return node;
    }
}
