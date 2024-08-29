package eu.metacloudservice.bungee;

import eu.metacloudservice.bungee.listener.CloudListener;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.webserver.RestDriver;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBootstrap extends Plugin {

    private static  BungeeBootstrap instance;
    private LiveService liveService;
    private RestDriver restDriver;
    public BungeeAudiences audiences;

    @Override
    public void onEnable() {
        instance = this;
        audiences = BungeeAudiences.builder(instance).build();
        liveService = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        restDriver = new RestDriver(liveService.getManagerAddress(), liveService.getRestPort());
        CloudAPI.getInstance().registerListener(new CloudListener());
    }

    public RestDriver getRestDriver() {
        return restDriver;
    }

    public static BungeeBootstrap getInstance() {
        return instance;
    }
}
