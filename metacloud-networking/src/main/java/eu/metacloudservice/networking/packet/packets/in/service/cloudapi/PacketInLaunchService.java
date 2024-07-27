/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.cloudapi;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInLaunchService extends Packet {

    @Getter
    private String group;

    public PacketInLaunchService() {
        setPacketUUID(92123);
    }

    public PacketInLaunchService(String group) {
        setPacketUUID(92123);
        this.group = group;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        group = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(group);
    }
}
