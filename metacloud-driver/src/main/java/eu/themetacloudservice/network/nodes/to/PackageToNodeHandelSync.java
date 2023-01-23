package eu.themetacloudservice.network.nodes.to;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageToNodeHandelSync extends Packet {

    private String service;

    public PackageToNodeHandelSync() {
        setPacketUUID(0223424523);
    }


    public PackageToNodeHandelSync(String service) {
        setPacketUUID(0223424523);
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
