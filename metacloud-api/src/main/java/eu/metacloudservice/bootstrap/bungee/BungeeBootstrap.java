package eu.metacloudservice.bootstrap.bungee;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.PacketInServiceDisconnect;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServiceList;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServices;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.TimerTask;

public class BungeeBootstrap extends Plugin {

    private static BungeeBootstrap instance;
    private BungeeAudiences audiences;

    @Override
    public void onLoad() {
        new AsyncCloudAPI();
        new CloudAPI(false);
    }

    @Override
    public void onEnable() {
        instance = this;
        this.audiences =  BungeeAudiences.builder(BungeeBootstrap.getInstance()).build();

    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPlayers().forEach(player -> {
            player.disconnect("cloudservice-shutdown");
        });
        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInServiceDisconnect(service.getService()));
    }

    public BungeeAudiences getAudiences() {
        return audiences;
    }

    public static BungeeBootstrap getInstance() {
        return instance;
    }

}
