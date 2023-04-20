package eu.metacloudservice.serverside.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitBootstrap extends JavaPlugin {

    private static BukkitBootstrap instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static BukkitBootstrap getInstance() {
        return instance;
    }
}
