package eu.metacloudservice.serverside.bungee;

import eu.metacloudservice.api.CloudPermsPool;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBootstrap extends Plugin {

    private static BungeeBootstrap instance;

    @Override
    public void onEnable() {
        instance = this;
        new CloudPermsPool();
    }

    public static BungeeBootstrap getInstance() {
        return instance;
    }
}
