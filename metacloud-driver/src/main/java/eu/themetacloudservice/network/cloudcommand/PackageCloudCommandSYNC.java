package eu.themetacloudservice.network.cloudcommand;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageCloudCommandSYNC extends Packet {

    private String  service;

    public PackageCloudCommandSYNC() {
        setPacketUUID(982498923);
    }

    public PackageCloudCommandSYNC(String service) {
        setPacketUUID(982498923);
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
