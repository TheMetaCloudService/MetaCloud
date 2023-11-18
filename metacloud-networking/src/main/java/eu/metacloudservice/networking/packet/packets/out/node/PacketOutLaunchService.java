/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketOutLaunchService extends Packet {

    @Getter
    private String service;
    @Getter
    private String group;
    @Getter
    private boolean useProtocol;

    public PacketOutLaunchService() {
        setPacketUUID(7843215);
    }

    public PacketOutLaunchService(String service, String group, boolean useProtocol) {
        setPacketUUID(7843215);
        this.service = service;
        this.group = group;
        this.useProtocol = useProtocol;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        this.service = buffer.readString();
        this.group = buffer.readString();
        this.useProtocol = buffer.readBoolean();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(this.service);
        buffer.writeString(this.group);
        buffer.writeBoolean(this.useProtocol);
    }
}
