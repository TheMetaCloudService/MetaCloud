package eu.metacloudservice.bootstrap.velocity.networking;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.bootstrap.velocity.VelocityBootstrap;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutCloudPlayerComponent;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketOutCloudPlayerComponent implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutCloudPlayerComponent){
            if (VelocityBootstrap.proxyServer.getPlayer(((PacketOutCloudPlayerComponent) packet).getPlayer()).isPresent()){
                Player player =   VelocityBootstrap.proxyServer.getPlayer(((PacketOutCloudPlayerComponent) packet).getPlayer()).get();
                player.sendMessage(((PacketOutCloudPlayerComponent) packet).getComponent());
            }
        }
    }
}
