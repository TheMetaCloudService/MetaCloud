/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import org.jetbrains.annotations.NotNull;

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
    public void readPacket(@NotNull NettyBuffer buffer) {
        service = buffer.readString();
        line = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(service);
        buffer.writeString(line);
    }
}
