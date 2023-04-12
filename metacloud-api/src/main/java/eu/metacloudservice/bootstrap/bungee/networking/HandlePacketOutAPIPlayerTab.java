package eu.metacloudservice.bootstrap.bungee.networking;

import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerMessage;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerTab;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HandlePacketOutAPIPlayerTab implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerTab) {
            if (ProxyServer.getInstance().getPlayer(((PacketOutAPIPlayerTab) packet).getUsername()).isConnected()){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(((PacketOutAPIPlayerTab) packet).getUsername());
                player.setTabHeader(TextComponent.fromLegacyText(((PacketOutAPIPlayerTab) packet).getHeader()), TextComponent.fromLegacyText(((PacketOutAPIPlayerTab) packet).getFooter()));
            }
        }
    }
}
