package eu.metacloudservice.serverside.bukkit;

import eu.metacloudservice.api.CloudPermsPool;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitBootstrap extends JavaPlugin {

    private static BukkitBootstrap instance;

    @Override
    public void onEnable() {
        instance = this;
        new CloudPermsPool();
    }

    public static BukkitBootstrap getInstance() {
        return instance;
    }
}
