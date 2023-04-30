package eu.metacloudservice.serverside.velocity;


import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.metacloudservice.api.CloudPermsPool;

@Plugin(id = "metacloudpermissions", name = "MetaCloud-Permissions", authors = "RauchigesEtwas", version = "1.0.0")
public class VeloCityBootstrap {

    private final ProxyServer server;

    @Inject
    public VeloCityBootstrap(ProxyServer proxyServer) {
        server = proxyServer;
    }


    @Subscribe
    private void handle(ProxyInitializeEvent event){
        new CloudPermsPool();

    }

    public ProxyServer getServer() {
        return server;
    }
}
