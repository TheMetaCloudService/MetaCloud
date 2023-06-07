package eu.metacloudservice.serverside.bukkit;

import eu.metacloudservice.webserver.RestDriver;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitBootstrap extends JavaPlugin {

    private  RestDriver restDriver;
    private static BukkitBootstrap instance;


    @Override
    public void onEnable() {
        instance = this;

    }
    public static BukkitBootstrap getInstance() {
        return instance;
    }


    public RestDriver getRestDriver() {
        return restDriver;
    }
}
