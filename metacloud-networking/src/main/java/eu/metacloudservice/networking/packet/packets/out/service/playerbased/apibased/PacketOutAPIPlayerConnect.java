/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketOutAPIPlayerConnect extends Packet {


    @Getter
    private String username;
    @Getter
    private String service;

    public PacketOutAPIPlayerConnect() {
        setPacketUUID(231231);
    }

    public PacketOutAPIPlayerConnect(String username, String service) {
        setPacketUUID(231231);
        this.username = username;
        this.service = service;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        this.username = buffer.readString();
        this.service = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(username);
        buffer.writeString(service);
    }
}
