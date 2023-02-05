package eu.themetacloudservice.network.service;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageServiceShutdown extends Packet {

    private String name;

    public PackageServiceShutdown() {
        setPacketUUID(2912453);
    }

    public PackageServiceShutdown(String name) {
        setPacketUUID(2912453);
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
