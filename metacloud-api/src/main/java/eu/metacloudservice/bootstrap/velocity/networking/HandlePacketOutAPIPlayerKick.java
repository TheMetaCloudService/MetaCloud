package eu.metacloudservice.bootstrap.velocity.networking;

import com.velocitypowered.api.util.MessagePosition;
import eu.metacloudservice.bootstrap.velocity.VelocityBootstrap;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerActionBar;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerKick;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;

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
