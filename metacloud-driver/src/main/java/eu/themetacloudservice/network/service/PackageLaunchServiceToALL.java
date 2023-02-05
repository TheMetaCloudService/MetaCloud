package eu.themetacloudservice.network.service;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageLaunchServiceToALL extends Packet  {

    private String name;
    private String group;
    private int port;
    private String node;
    private String address;
    private String type;


    public PackageLaunchServiceToALL() {
        setPacketUUID(452981298);
    }

    public PackageLaunchServiceToALL(String name, String group, int port, String node, String address, String type) {
        setPacketUUID(452981298);
        this.name = name;
        this.group = group;
        this.port = port;
        this.node = node;
        this.address = address;
        this.type = type;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        name = buffer.readString();
        group = buffer.readString();
        port = buffer.readInt();
        node = buffer.readString();
        address =buffer.readString();
        type =buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(name);
        buffer.writeString(group);
        buffer.writeInt(port);
        buffer.writeString(node);
        buffer.writeString(address);
        buffer.writeString(type);
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public int getPort() {
        return port;
    }

    public String getNode() {
        return node;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }
}
