/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bungee;

import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.bungee.command.PermissionCommand;
import eu.metacloudservice.bungee.listener.PermissionListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBootstrap extends Plugin {

    @Override
    public void onEnable() {

        new CloudPermissionAPI();
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PermissionListener());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PermissionCommand("perms"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PermissionCommand("mperms"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PermissionCommand("permissions"));
    }
}
