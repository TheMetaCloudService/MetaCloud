/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInServiceReaction extends Packet {

    @Getter
    private String  service;


    public PacketInServiceReaction() {
        setPacketUUID(2209492);
    }

    public PacketInServiceReaction(String service) {
        setPacketUUID(2209492);
        this.service = service;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        service = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
            buffer.writeString(service);
    }
}
