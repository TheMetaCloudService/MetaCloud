package eu.themetacloudservice.network.service;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageShutdownServiceToALL extends Packet {

    private String name;

    public PackageShutdownServiceToALL() {
        setPacketUUID(9128312);
    }

    public PackageShutdownServiceToALL(String name) {
        setPacketUUID(9128312);
        this.name = name;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        name = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(name);
    }

    public String getName() {
        return name;
    }
}
