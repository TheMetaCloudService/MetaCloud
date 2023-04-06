package eu.metacloudservice.bootstrap.bungee.networking;

import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerConnect;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerKick;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HandlePacketOutAPIPlayerKick implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerKick) {
            if (ProxyServer.getInstance().getPlayer(((PacketOutAPIPlayerKick) packet).getUsername()).isConnected()){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(((PacketOutAPIPlayerKick) packet).getUsername());
                player.disconnect(((PacketOutAPIPlayerKick) packet).getMessage());
            }
        }
    }
}
