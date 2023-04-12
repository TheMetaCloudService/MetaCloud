package eu.metacloudservice.networking.out.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutAPIPlayerTab extends Packet {

    private String username;
    private String header;
    private String footer;


    public PacketOutAPIPlayerTab() {
        setPacketUUID(873457882);
    }

    public PacketOutAPIPlayerTab(String username, String header, String footer) {
        setPacketUUID(873457882);
        this.username = username;
        this.header = header;
        this.footer = footer;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        username = buffer.readString();
        header = buffer.readString();
        footer = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(username);
        buffer.writeString(header);
        buffer.writeString(footer);
    }

    public String getUsername() {
        return username;
    }

    public String getHeader() {
        return header;
    }

    public String getFooter() {
        return footer;
    }
}
