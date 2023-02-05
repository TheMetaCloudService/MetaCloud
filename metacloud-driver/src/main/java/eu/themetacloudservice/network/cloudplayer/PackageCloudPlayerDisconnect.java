package eu.themetacloudservice.network.cloudplayer;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageCloudPlayerDisconnect extends Packet {


    private String uuid;
    private String name;

    public PackageCloudPlayerDisconnect() {
        setPacketUUID(192351);
    }

    public PackageCloudPlayerDisconnect(String uuid, String name) {
        setPacketUUID(192351);
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        uuid = buffer.readString();
        name = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(uuid);
        buffer.writeString(name);
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}
