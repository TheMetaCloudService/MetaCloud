package eu.metacloudservice.networking.in.service.playerbased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInPlayerConnect extends Packet {

    @Getter
    private String name;

    @Getter
    private String proxy;

    public PacketInPlayerConnect() {
        setPacketUUID(2995985);
    }

    public PacketInPlayerConnect(String name, String proxy) {
        setPacketUUID(2995985);
        this.name = name;
        this.proxy = proxy;
    }

    public void readPacket(NettyBuffer buffer) {
        this.name = buffer.readString();
        this.proxy = buffer.readString();
    }

    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(this.name);
        buffer.writeString(this.proxy);
    }

}
