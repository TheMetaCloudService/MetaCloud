package eu.metacloudservice.bootstrap.bungee.networking;

import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerKick;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerMessage;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HandlePacketOutAPIPlayerMessage implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerMessage) {
            if (ProxyServer.getInstance().getPlayer(((PacketOutAPIPlayerMessage) packet).getUsername()).isConnected()){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(((PacketOutAPIPlayerMessage) packet).getUsername());
                player.sendMessage(((PacketOutAPIPlayerMessage) packet).getMessage());
            }
        }
    }
}
