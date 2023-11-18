package eu.metacloudservice.bootstrap.velocity.networking;

import eu.metacloudservice.bootstrap.velocity.VelocityBootstrap;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerTab;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;

public class HandlePacketOutAPIPlayerTab implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerTab){
            if (VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerTab) packet).getUsername()).get() != null){
                VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerTab) packet).getUsername()).get().getTabList().setHeaderAndFooter(Component.text(((PacketOutAPIPlayerTab) packet).getHeader()), Component.text(((PacketOutAPIPlayerTab) packet).getFooter()));
            }
        }

    }
}
