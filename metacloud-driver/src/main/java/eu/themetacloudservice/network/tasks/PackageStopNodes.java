package eu.themetacloudservice.network.tasks;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageStopNodes extends Packet {

    public PackageStopNodes() {
        setPacketUUID(4398912);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {

    }

    @Override
    public void writePacket(NettyBuffer buffer) {

    }
}
