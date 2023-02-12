package eu.themetacloudservice.network.cloudplayer;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageCloudPlayerChangeService extends Packet {

    private String name;
    private String server;
    private String from;


    public PackageCloudPlayerChangeService() {
        setPacketUUID(9829191);
    }

    public PackageCloudPlayerChangeService(String name, String server, String from) {
        setPacketUUID(9829191);
        this.name = name;
        this.server = server;
        this.from = from;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        name = buffer.readString();
        server = buffer.readString();
        from = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {

        buffer.writeString(name);
        buffer.writeString(server);
        buffer.writeString(from);

    }


    public String getFrom() {
        return from;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }
}
