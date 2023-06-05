package eu.metacloudservice.networking.out.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketOutCloudProxyChangeState extends Packet {

    @Getter
    private String name;

    @Getter
    private String state;


    public PacketOutCloudProxyChangeState() {
        setPacketUUID(90923412);
    }

    public PacketOutCloudProxyChangeState(String name, String state) {
        setPacketUUID(90923412);
        this.name = name;
        this.state = state;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        this.name = buffer.readString();
        this.state = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(name);
        buffer.writeString(state);
    }
}
