package eu.metacloudservice.bootstrap.bungee.networking;

import eu.metacloudservice.bootstrap.bungee.BungeeBootstrap;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutCloudPlayerComponent;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HandlePacketOutCloudPlayerComponent implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutCloudPlayerComponent){
            if (ProxyServer.getInstance().getPlayer(((PacketOutCloudPlayerComponent) packet).getPlayer()).isConnected()){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(((PacketOutCloudPlayerComponent) packet).getPlayer());
                BungeeBootstrap.getInstance().getAudiences().player(player).sendMessage(((PacketOutCloudPlayerComponent) packet).getComponent());
            }
        }
    }
}
