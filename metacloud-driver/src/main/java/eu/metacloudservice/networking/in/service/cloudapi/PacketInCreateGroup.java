package eu.metacloudservice.networking.in.service.cloudapi;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInCreateGroup extends Packet {

    @Getter
    private String groupConfig;

    public PacketInCreateGroup() {
        setPacketUUID(398293);
    }

    public PacketInCreateGroup(String groupConfig) {
        setPacketUUID(398293);
        this.groupConfig = groupConfig;
    }


    @Override
    public void readPacket(NettyBuffer buffer) {
        groupConfig = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(groupConfig);
    }
}
