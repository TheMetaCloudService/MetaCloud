package eu.metacloudservice.networking.out.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutEnableConsole extends Packet {

    private String service;


    public PacketOutEnableConsole() {
        setPacketUUID(240012);
    }

    public PacketOutEnableConsole(String service) {
        setPacketUUID(240012);
        this.service = service;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        service = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(service);
    }

    public String getService() {
        return service;
    }
}
