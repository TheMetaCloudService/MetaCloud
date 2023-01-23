package eu.themetacloudservice.network.nodes.to;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageToNodeHandelServiceExit extends Packet {

    private String service;

    public PackageToNodeHandelServiceExit() {
        setPacketUUID(87432921);
    }

    public PackageToNodeHandelServiceExit(String service) {
        setPacketUUID(87432921);
        this.service = service;

    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        service = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(service);
    }

    public String getService() {
        return service;
    }
}
