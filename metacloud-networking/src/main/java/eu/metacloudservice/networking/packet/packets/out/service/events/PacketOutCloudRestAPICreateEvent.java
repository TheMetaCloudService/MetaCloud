package eu.metacloudservice.networking.packet.packets.out.service.events;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import org.jetbrains.annotations.NotNull;

public class PacketOutCloudRestAPICreateEvent extends Packet {

    @lombok.Getter
    private String path;
    @lombok.Getter
    private  String content;

    public PacketOutCloudRestAPICreateEvent() {
        setPacketUUID(904303032);
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        path = buffer.readString();
        content = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(path);
        buffer.writeString(content);
    }

    public PacketOutCloudRestAPICreateEvent(String path, String content) {
        setPacketUUID(904303032);
        this.path = path;
        this.content = content;
    }
}
