/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.units.qual.C;


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
        component = (Component) buffer.readClass(Component.class);
        player = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeClass(component);
        buffer.writeString(player);
    }
}
