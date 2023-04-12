package eu.metacloudservice.bootstrap.velocity.networking;

import eu.metacloudservice.bootstrap.velocity.VelocityBootstrap;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerMessage;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;

public class HandlePacketOutAPIPlayerMessage implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerMessage) {
            if (VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerMessage) packet).getUsername()).get() != null){
                VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerMessage) packet).getUsername()).get().sendMessage(Component.text(((PacketOutAPIPlayerMessage) packet).getMessage()));
            }
        }
    }
}
