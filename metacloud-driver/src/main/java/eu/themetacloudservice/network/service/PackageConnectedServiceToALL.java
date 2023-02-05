package eu.themetacloudservice.network.service;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageConnectedServiceToALL extends Packet {

    private String name;
    private String group;
    private int port;
    private String node;
    private String address;
    private String type;
    private String groupConfig;

    public PackageConnectedServiceToALL() {
        setPacketUUID(845828123);
    }

    public PackageConnectedServiceToALL(String name, String group, int port, String node, String address, String type, String groupConfig) {
        setPacketUUID(845828123);
        this.name = name;
        this.group = group;
        this.port = port;
        this.node = node;
        this.address = address;
        this.type = type;
        this.groupConfig = groupConfig;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {

        name = buffer.readString();
        group = buffer.readString();
        port = buffer.readInt();
        node = buffer.readString();
        address =buffer.readString();
        type =buffer.readString();
        groupConfig =buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(name);
        buffer.writeString(group);
        buffer.writeInt(port);
        buffer.writeString(node);
        buffer.writeString(address);
        buffer.writeString(type);
        buffer.writeString(groupConfig);
    }

    public String getGroupConfig() {
        return groupConfig;
    }

    public String getType() {
        return type;
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
}
