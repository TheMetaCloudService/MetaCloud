package eu.metacloudservice.networking.packet.packets.in.service.cloudapi;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketLaunchServiceWithCustomTemplate extends Packet {

    @Getter
    private String group;
    @Getter
    private String template;


    public PacketLaunchServiceWithCustomTemplate() {
        setPacketUUID(439021234);
    }

    public PacketLaunchServiceWithCustomTemplate(String group, String template) {
        setPacketUUID(439021234);
        this.group = group;
        this.template = template;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        group = buffer.readString();
        template = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(group);
        buffer.writeString(template);
    }
}
