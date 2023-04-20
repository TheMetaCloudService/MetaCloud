package eu.metacloudservice.networking.out.service.group;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutGroupCreate extends Packet {



    private String group;


    public PacketOutGroupCreate() {
        setPacketUUID(82342732);
    }

    public PacketOutGroupCreate(String group) {
        setPacketUUID(82342732);
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
