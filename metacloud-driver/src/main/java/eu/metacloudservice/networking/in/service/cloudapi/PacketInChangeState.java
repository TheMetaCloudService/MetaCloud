package eu.metacloudservice.networking.in.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInChangeState extends Packet {

    @Getter
    private String service;

    @Getter
    private String state;


    public PacketInChangeState() {
        setPacketUUID(893221);
    }

    public PacketInChangeState(String service, String state) {
        setPacketUUID(893221);
        this.service = service;
        this.state = state;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        this.service = buffer.readString();
        this.state = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(service);
        buffer.writeString(state);
    }
}
