package eu.metacloudservice.bootstrap.velocity.networking;

import eu.metacloudservice.bootstrap.velocity.VelocityBootstrap;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketOutAPIPlayerDispactchCommand;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerConnect;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HandlePacketOutAPIPlayerDispactchCommand implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerDispactchCommand) {
            if (VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerDispactchCommand) packet).getUserName()).get() != null){
               VelocityBootstrap.proxyServer.getCommandManager().executeAsync( VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerDispactchCommand) packet).getUserName()).get(), ((PacketOutAPIPlayerDispactchCommand) packet).getCommand());
            }
        }
    }
}
