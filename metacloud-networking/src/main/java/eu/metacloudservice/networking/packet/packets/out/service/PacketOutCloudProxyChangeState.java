/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutCloudProxyChangeState extends Packet {

    @Getter
    private String name;

    @Getter
    private String state;


    public PacketOutCloudProxyChangeState() {
        setPacketUUID(90923412);
    }

    public PacketOutCloudProxyChangeState(String name, String state) {
        setPacketUUID(90923412);
        this.name = name;
        this.state = state;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        this.name = buffer.readString();
        this.state = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(name);
        buffer.writeString(state);
    }
}
