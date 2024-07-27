/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.in.service.cloudapi;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketInChangeState extends Packet {

    @Getter
    private String service;

    @Getter
    private String state;


    public PacketInChangeState() {
        setPacketUUID(893221);
    }

    public PacketInChangeState(String service, String state) {
        setPacketUUID(893221);
        this.service = service;
        this.state = state;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.service = buffer.readString();
        this.state = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(service);
        buffer.writeString(state);
    }
}
