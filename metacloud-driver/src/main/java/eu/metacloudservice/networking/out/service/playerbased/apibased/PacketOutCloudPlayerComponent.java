package eu.metacloudservice.networking.in.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;


public class PacketOutCloudPlayerComponent extends Packet {

    @Getter
    private Component component;
    @Getter
    private String player;


    public PacketOutCloudPlayerComponent() {
        setPacketUUID(234893298);
    }

    public PacketOutCloudPlayerComponent(Component component, String player) {
        setPacketUUID(234893298);
        this.component = component;
        this.player = player;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        component = (CloudComponent) buffer.readClass(CloudComponent.class);
        player = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeClass(component);
        buffer.writeString(player);
    }
}
