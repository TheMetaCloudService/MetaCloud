package eu.metacloudservice.bootstrap.velocity.networking;

import eu.metacloudservice.bootstrap.velocity.VelocityBootstrap;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerKick;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketOutAPIPlayerKick implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerKick) {
            if (VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerKick) packet).getUsername()).get() != null){
                VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerKick) packet).getUsername()).get().disconnect(Component.text(((PacketOutAPIPlayerKick) packet).getMessage()));
            }
        }
    }
}
