/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInNodeActionSuccess extends Packet {

    @Getter
    private boolean launched;
    @Getter
    private String service;
    @Getter
    private String node;
    @Getter
    private int port;

    public PacketInNodeActionSuccess() {
        setPacketUUID(392921);
    }

    public PacketInNodeActionSuccess(boolean launched, String service, String node, int port) {
        setPacketUUID(392921);
        this.launched = launched;
        this.service = service;
        this.node = node;
        this.port = port;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.launched = buffer.readBoolean();
        this.service = buffer.readString();
        this.node = buffer.readString();
        this.port = buffer.readInt();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeBoolean(this.launched);
        buffer.writeString(this.service);
        buffer.writeString(this.node);
        buffer.writeInt(this.port);
    }
}
