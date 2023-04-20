package eu.metacloudservice.networking.out.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketOutServiceLaunch extends Packet {
    @Getter
    private String name;
    @Getter
    private boolean isProxy;

    @Getter
    private String group;
    @Getter
    private String node;

    public PacketOutServiceLaunch() {
        setPacketUUID(904359092);
    }

    public PacketOutServiceLaunch(String name, boolean isProxy, String group, String node) {
        setPacketUUID(904359092);
        this.name = name;
        this.isProxy = isProxy;
        this.group = group;
        this.node = node;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        name = buffer.readString();
        isProxy = buffer.readBoolean();
        node = buffer.readString();
        node = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(name);
        buffer.writeBoolean(isProxy);
        buffer.writeString(node);
        buffer.writeString(node);
    }

    public String getName() {
        return name;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public String getGroup() {
        return group;
    }

    public String getNode() {
        return node;
    }
}
