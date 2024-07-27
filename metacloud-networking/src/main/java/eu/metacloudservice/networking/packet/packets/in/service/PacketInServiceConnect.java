/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInServiceConnect extends Packet {

    @Getter
    private String service;

    public PacketInServiceConnect() {
        setPacketUUID(239945);
    }

    public PacketInServiceConnect(String service) {
        setPacketUUID(239945);
        this.service = service;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.service = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(this.service);
    }
}
