package eu.metacloudservice.bootstrap.bungee;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bootstrap.bungee.listener.CloudEvents;
import eu.metacloudservice.bootstrap.bungee.networking.HandlePacketOutAPIPlayerConnect;
import eu.metacloudservice.bootstrap.bungee.networking.HandlePacketOutAPIPlayerKick;
import eu.metacloudservice.bootstrap.bungee.networking.HandlePacketOutAPIPlayerMessage;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.PacketInServiceDisconnect;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerConnect;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerKick;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerMessage;
import eu.metacloudservice.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.storage.UUIDDriver;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BungeeBootstrap extends Plugin {

    private static BungeeBootstrap instance;

    @Override
    public void onEnable() {
        instance = this;
        new CloudAPI();

    }

    @Override
    public void onDisable() {
        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInServiceDisconnect(service.getService()));
    }

    public static BungeeBootstrap getInstance() {
        return instance;
    }
}
