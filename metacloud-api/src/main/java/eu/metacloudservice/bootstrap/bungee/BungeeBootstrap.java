package eu.metacloudservice.bootstrap.bungee;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.PacketInServiceDisconnect;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBootstrap extends Plugin {

    private static BungeeBootstrap instance;


    @Override
    public void onLoad() {
        new AsyncCloudAPI();
        new CloudAPI(false);
    }

    @Override
    public void onEnable() {
        instance = this;
        ProxyServer.getInstance().getConsole().sendMessage("[§bMetaCloud§r] >");
        ProxyServer.getInstance().getConsole().sendMessage("[§bMetaCloud§r] > THE METACLOUD has been loaded with Succsess");
        ProxyServer.getInstance().getConsole().sendMessage("[§bMetaCloud§r] > This registered all Classes & files..");
        ProxyServer.getInstance().getConsole().sendMessage("[§bMetaCloud§r] >");



    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPlayers().forEach(player -> {
            player.disconnect("cloudservice-shutdown");
        });
        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInServiceDisconnect(service.getService()));
    }

    public static BungeeBootstrap getInstance() {
        return instance;
    }
}
