package eu.themetacloudservice.network.service;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageRegisterServiceToALL extends Packet {

    private String  name;
    private String node;

    public PackageRegisterServiceToALL() {
        setPacketUUID(98439812);
    }

    public PackageRegisterServiceToALL(String name, String node) {
        setPacketUUID(98439812);
        this.name = name;
        this.node = node;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        name = buffer.readString();
        node = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(name);
        buffer.writeString(node);
    }

    public String getName() {
        return name;
    }

    public String getNode() {
        return node;
    }
}
