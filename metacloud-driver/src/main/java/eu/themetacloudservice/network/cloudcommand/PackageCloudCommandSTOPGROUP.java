package eu.themetacloudservice.network.cloudcommand;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageCloudCommandSTOPGROUP extends Packet {
    private String  group;

    public PackageCloudCommandSTOPGROUP() {
        setPacketUUID(299384823);
    }

    public PackageCloudCommandSTOPGROUP(String group) {
        setPacketUUID(299384823);
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
