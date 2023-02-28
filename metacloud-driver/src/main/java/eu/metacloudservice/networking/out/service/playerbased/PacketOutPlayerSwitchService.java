package eu.metacloudservice.networking.out.service.playerbased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketOutPlayerSwitchService extends Packet {


    @Getter
    private String name;
    @Getter
    private String server;
    @Getter
    private String from;

    public PacketOutPlayerSwitchService() {
        setPacketUUID(6332);
    }

    public PacketOutPlayerSwitchService(String name, String server, String from) {
        setPacketUUID(6332);
        this.name = name;
        this.server = server;
        this.from = from;
    }

    public void readPacket(NettyBuffer buffer) {
        this.name = buffer.readString();
        this.server = buffer.readString();
        this.from = buffer.readString();
    }

    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(this.name);
        buffer.writeString(this.server);
        buffer.writeString(this.from);
    }


}
