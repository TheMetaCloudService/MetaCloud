package eu.themetacloudservice.network.cloudcommand;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageCloudCommandMAINTENANCE  extends Packet {

    private String group;

    public PackageCloudCommandMAINTENANCE() {
        setPacketUUID(98329982);
    }

    public PackageCloudCommandMAINTENANCE(String group) {
        setPacketUUID(98329982);
        this.group = group;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        group = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(group);
    }

    public String getGroup() {
        return group;
    }
}
