package eu.metacloudservice.bootstrap.velocity.networking;

import eu.metacloudservice.bootstrap.velocity.VelocityBootstrap;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased.PacketOutAPIPlayerDispactchCommand;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketOutAPIPlayerDispactchCommand implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerDispactchCommand) {
            if (VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerDispactchCommand) packet).getUserName()).isPresent()){
               VelocityBootstrap.proxyServer.getCommandManager().executeAsync( VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerDispactchCommand) packet).getUserName()).get(), ((PacketOutAPIPlayerDispactchCommand) packet).getCommand());
            }
        }
    }
}
