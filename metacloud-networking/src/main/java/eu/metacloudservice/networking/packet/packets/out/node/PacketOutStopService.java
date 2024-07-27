/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutStopService extends Packet {

    @Getter
    private String service;

    public PacketOutStopService() {
        setPacketUUID(87432921);
    }

    public PacketOutStopService(String service) {
        setPacketUUID(87432921);
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
