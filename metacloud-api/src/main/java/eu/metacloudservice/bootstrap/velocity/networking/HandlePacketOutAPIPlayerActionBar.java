package eu.metacloudservice.bootstrap.velocity.networking;

import eu.metacloudservice.bootstrap.velocity.VelocityBootstrap;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerActionBar;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketOutAPIPlayerActionBar implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerActionBar){
         if (VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerActionBar) packet).getUsername()).get() != null){
            VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerActionBar) packet).getUsername()).get().sendActionBar(Component.text(((PacketOutAPIPlayerActionBar) packet).getMessage()));
         }
        }
    }
}
