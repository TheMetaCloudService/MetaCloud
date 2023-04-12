package eu.metacloudservice.bootstrap.velocity.networking;

import eu.metacloudservice.bootstrap.velocity.VelocityBootstrap;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerConnect;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketOutAPIPlayerConnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerConnect) {
            if (VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerConnect) packet).getUsername()).get() != null){
                VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerConnect) packet).getUsername()).get().createConnectionRequest(VelocityBootstrap.proxyServer.getServer(((PacketOutAPIPlayerConnect) packet).getService()).get());
            }
        }
    }
}
