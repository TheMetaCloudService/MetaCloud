/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bungee;

import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.api.PluginDriver;
import eu.metacloudservice.bungee.listener.PermissionListener;
import eu.metacloudservice.subcommand.PermissionCommand;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Comparator;
import java.util.Objects;

public class BungeeBootstrap extends Plugin {

    public BungeeAudiences audiences;
    private static BungeeBootstrap instance;
    @Override
    public void onLoad() {
        new CloudPermissionAPI();
    }

    @Override
    public void onEnable() {
        instance = this;
        audiences = BungeeAudiences.builder(instance).build();
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PermissionListener());
        PluginDriver.getInstance().register(new PermissionCommand());
    }

    public static BungeeBootstrap getInstance() {
        return instance;
    }
}
