/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutDisableConsole extends Packet {

    private String service;


    public PacketOutDisableConsole() {
        setPacketUUID(2323244);
    }

    public PacketOutDisableConsole(String service) {
        setPacketUUID(2323244);
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
