package eu.metacloudservice.bootstrap.bungee.networking;

import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerTitle;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HandlePacketOutAPIPlayerTitle implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerTitle){
            if (ProxyServer.getInstance().getPlayer(((PacketOutAPIPlayerTitle) packet).getUsername()).isConnected()){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(((PacketOutAPIPlayerTitle) packet).getUsername());
                Title title = ProxyServer.getInstance().createTitle();
                title.title(new TextComponent(((PacketOutAPIPlayerTitle) packet).getTitle()));
                title.subTitle(new TextComponent(((PacketOutAPIPlayerTitle) packet).getSubTitle()));
                title.fadeIn(((PacketOutAPIPlayerTitle) packet).getFadeIn());
                title.stay(((PacketOutAPIPlayerTitle) packet).getStay());
                title.fadeOut(((PacketOutAPIPlayerTitle) packet).getFadeOut());
                player.sendTitle(title);
            }
        }

    }
}
