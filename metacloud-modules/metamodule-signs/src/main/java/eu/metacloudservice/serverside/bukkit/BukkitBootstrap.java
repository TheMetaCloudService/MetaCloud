package eu.metacloudservice.serverside.bukkit;

import eu.metacloudservice.api.SignsAPI;
import eu.metacloudservice.serverside.bukkit.signs.SignDriver;
import eu.metacloudservice.webserver.RestDriver;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitBootstrap extends JavaPlugin {

    private static BukkitBootstrap instance;
    private SignDriver signDriver;
    private SignsAPI signsAPI;


    @Override
    public void onEnable() {
        instance = this;
        signDriver = new SignDriver();
        signsAPI = new SignsAPI();

    }
    public static BukkitBootstrap getInstance() {
        return instance;
    }

    public SignDriver getSignDriver() {
        return signDriver;
    }

    public SignsAPI getSignsAPI() {
        return signsAPI;
    }
}
