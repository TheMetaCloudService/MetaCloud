package eu.themetacloudservice.network.cloudcommand;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageCloudCommandPLAYERS extends Packet {

    private String  group;
    private Integer players;

    public PackageCloudCommandPLAYERS() {
        setPacketUUID(19342090);
    }

    public PackageCloudCommandPLAYERS(String group, Integer players) {
        setPacketUUID(19342090);
        this.group = group;
        this.players = players;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        group = buffer.readString();
        players = buffer.readInt();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(group);
        buffer.writeInt(players);
    }

    public String getGroup() {
        return group;
    }

    public Integer getPlayers() {
        return players;
    }
}
