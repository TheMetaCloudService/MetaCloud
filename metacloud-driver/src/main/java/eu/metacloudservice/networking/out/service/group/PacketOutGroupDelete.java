package eu.metacloudservice.networking.out.service.group;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutGroupDelete extends Packet {

    private String group;


    public PacketOutGroupDelete() {
        setPacketUUID(9849929);
    }

    public PacketOutGroupDelete(String group) {
        setPacketUUID(9849929);
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
