package eu.themetacloudservice.network.nodes.to;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageToNodeHandelKillNode extends Packet {

    public PackageToNodeHandelKillNode() {
        setPacketUUID(78342812);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {

    }

    @Override
    public void writePacket(NettyBuffer buffer) {

    }
}
