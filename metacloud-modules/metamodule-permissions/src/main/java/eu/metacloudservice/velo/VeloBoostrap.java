/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.velo;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.api.PluginDriver;
import eu.metacloudservice.subcommand.PermissionCommand;
import eu.metacloudservice.velo.listener.PermissionListener;
import lombok.NonNull;



@Plugin(id = "permissions", name = "metacloud-permissions", version = "1.0.9-RELEASE", authors = "RauchigesEtwas", dependencies = {@Dependency(id = "metacloudapi"), @Dependency(id = "metacloudplugin")})
public class VeloBoostrap {

    private static ProxyServer proxyServer;

    @Inject
    public VeloBoostrap(@NonNull ProxyServer proxyServer) {
        new CloudPermissionAPI();
        VeloBoostrap.proxyServer = proxyServer;
    }

    public static ProxyServer getProxyServer() {
        return proxyServer;
    }

    @Subscribe
    public void handelInject(ProxyInitializeEvent event){
        proxyServer.getEventManager().register(this, new PermissionListener(new PermissionBaseVelocity()));
        PluginDriver.getInstance().register(new PermissionCommand());
    }

}
