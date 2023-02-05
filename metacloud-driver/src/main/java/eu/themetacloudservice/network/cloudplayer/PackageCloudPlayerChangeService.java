package eu.themetacloudservice.network.cloudplayer;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageCloudPlayerChangeService extends Packet {

    private String name;
    private String server;


    public PackageCloudPlayerChangeService() {
        setPacketUUID(9829191);
    }

    public PackageCloudPlayerChangeService(String name, String server) {
        setPacketUUID(9829191);
        this.name = name;
        this.server = server;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        name = buffer.readString();
        server = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {

        buffer.writeString(name);
        buffer.writeString(server);

    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }
}
