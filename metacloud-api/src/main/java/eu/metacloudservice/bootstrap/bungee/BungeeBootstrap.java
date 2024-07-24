package eu.metacloudservice.bootstrap.bungee;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.CloudAPIEnvironment;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.service.PacketInServiceDisconnect;
import lombok.Getter;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class BungeeBootstrap extends Plugin {

    @Getter
    private static BungeeBootstrap instance;
    private BungeeAudiences audiences;

    @Override
    public void onLoad() {
        new CloudAPI();
    }

    @Override
    public void onEnable() {
        instance = this;
       final CloudAPIEnvironment environment = new CloudAPIEnvironment();
        environment.handleNettyConnection();
        environment.registerHandlers();
        environment.registerBungeeHandlers();
        environment.handelNettyUpdate();
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


}
