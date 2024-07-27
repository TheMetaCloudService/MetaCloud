/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import org.jetbrains.annotations.NotNull;

public class PacketInAPIPlayerTab extends Packet {
    private String username;
    private String header;
    private String footer;


    public PacketInAPIPlayerTab() {
        setPacketUUID(2143290823);
    }

    public PacketInAPIPlayerTab(String username, String header, String footer) {
        setPacketUUID(2143290823);
        this.username = username;
        this.header = header;
        this.footer = footer;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        username = buffer.readString();
        header = buffer.readString();
        footer = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
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
