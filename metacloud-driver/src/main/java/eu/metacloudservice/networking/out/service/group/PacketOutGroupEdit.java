package eu.metacloudservice.networking.out.service.group;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutGroupEdit extends Packet {
    private String group;


    public PacketOutGroupEdit() {
        setPacketUUID(43212312);
    }

    public PacketOutGroupEdit(String group) {
        setPacketUUID(43212312);
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
