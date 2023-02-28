package eu.metacloudservice.networking.in.service.cloudapi;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInLaunchService extends Packet {

    @Getter
    private String group;

    public PacketInLaunchService() {
        setPacketUUID(92123);
    }

    public PacketInLaunchService(String group) {
        setPacketUUID(92123);
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
}
