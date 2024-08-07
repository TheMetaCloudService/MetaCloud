/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutServiceDisconnected extends Packet {

    @Getter
    private String name;
    @Getter
    private boolean isProxy;


    public PacketOutServiceDisconnected() {
        setPacketUUID(291234);
    }

    public PacketOutServiceDisconnected(String name, boolean isProxy) {
        setPacketUUID(291234);
        this.name = name;
        this.isProxy = isProxy;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        name = buffer.readString();
        isProxy = buffer.readBoolean();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(name);
        buffer.writeBoolean(isProxy);
    }
}
