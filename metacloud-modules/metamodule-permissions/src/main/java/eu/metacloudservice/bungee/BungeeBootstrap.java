/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bungee;

import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.api.PluginDriver;
import eu.metacloudservice.bungee.listener.PermissionListener;
import eu.metacloudservice.subcommand.PermissionCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBootstrap extends Plugin {

    @Override
    public void onEnable() {

        new CloudPermissionAPI();
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PermissionListener());
        PluginDriver.getInstance().register(new PermissionCommand());
    }
}
