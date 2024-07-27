/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutServicePrepared extends Packet {

    @Getter
    private String name;
    @Getter
    private boolean isProxy;

    @Getter
    private String group;
    @Getter
    private String node;


    public PacketOutServicePrepared() {
        setPacketUUID(235112);
    }

    public PacketOutServicePrepared(String name, boolean isProxy, String group, String node) {
        setPacketUUID(235112);
        this.name = name;
        this.isProxy = isProxy;
        this.group = group;
        this.node = node;

    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        name = buffer.readString();
        isProxy = buffer.readBoolean();
        group = buffer.readString();
        node = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(name);
        buffer.writeBoolean(isProxy);
        buffer.writeString(group);
        buffer.writeString(node);

    }
}
