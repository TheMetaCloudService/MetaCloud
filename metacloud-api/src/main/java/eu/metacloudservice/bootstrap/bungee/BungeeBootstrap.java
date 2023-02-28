package eu.metacloudservice.bootstrap;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.PacketInServiceDisconnect;
import eu.metacloudservice.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.storage.UUIDDriver;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BungeeBootstrap extends Plugin {

    @Override
    public void onEnable() {
        new CloudAPI();

    }

    @Override
    public void onDisable() {
        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInServiceDisconnect(service.getService()));
    }
}
