package eu.metacloudservice.networking.in.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketInSendConsole extends Packet {

    private String service;
    private String line;


    public PacketInSendConsole() {
        setPacketUUID(230103);
    }

    public PacketInSendConsole(String service, String line) {
        setPacketUUID(230103);
        this.service = service;
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    public String getService() {
        return service;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        service = buffer.readString();
        line = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(service);
        buffer.writeString(line);
    }
}
