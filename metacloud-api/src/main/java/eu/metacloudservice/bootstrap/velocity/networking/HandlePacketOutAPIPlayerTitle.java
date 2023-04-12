package eu.metacloudservice.bootstrap.velocity.networking;


import eu.metacloudservice.bootstrap.velocity.VelocityBootstrap;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerTitle;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.units.qual.C;

import java.time.Duration;

public class HandlePacketOutAPIPlayerTitle implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutAPIPlayerTitle){
            if (VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerTitle) packet).getUsername()).get() != null){


                Title title = Title.title(Component.text(((PacketOutAPIPlayerTitle) packet).getTitle()), Component.text(((PacketOutAPIPlayerTitle) packet).getSubTitle()), Title.Times.of( Duration.ofSeconds(Long.getLong(String.valueOf(((PacketOutAPIPlayerTitle) packet).getFadeIn()))), Duration.ofSeconds(Long.getLong(String.valueOf(((PacketOutAPIPlayerTitle) packet).getStay()))),   Duration.ofSeconds(Long.getLong(String.valueOf(((PacketOutAPIPlayerTitle) packet).getFadeOut())))));

                VelocityBootstrap.proxyServer.getPlayer(((PacketOutAPIPlayerTitle) packet).getUsername()).get().getCurrentServer().get().getServer().showTitle(title);
            }
        }

    }
}
