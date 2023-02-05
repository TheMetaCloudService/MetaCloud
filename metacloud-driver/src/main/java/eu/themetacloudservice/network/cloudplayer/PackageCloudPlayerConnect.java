package eu.themetacloudservice.network.cloudplayer;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageCloudPlayerConnect extends Packet {

    private String uuid;
    private String name;
    private String proxy;


    public PackageCloudPlayerConnect() {
        setPacketUUID(919828381);
    }

    public PackageCloudPlayerConnect(String uuid, String name, String proxy) {
        setPacketUUID(919828381);
        this.uuid = uuid;
        this.name = name;
        this.proxy = proxy;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {

        this.uuid = buffer.readString();
        this.name =buffer.readString();
        this.proxy =buffer.readString();

    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(uuid);
        buffer.writeString(name);
        buffer.writeString(proxy);
    }


    public String getProxy() {
        return proxy;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}
