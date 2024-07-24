package eu.metacloudservice.bootstrap.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.CloudAPIEnvironment;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.service.PacketInServiceDisconnect;

import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

@Plugin(id = "metacloudapi", name = "metacloud-api", version = "1.1.1-RELEASE", authors = "RauchigesEtwas", description = "This is the api of the cloudsystem")
public class VelocityBootstrap {

    public static ProxyServer proxyServer;
    @Inject(optional = false)
    public VelocityBootstrap(ProxyServer proxyServer, Logger logger) {
        new CloudAPI();
        VelocityBootstrap.proxyServer = proxyServer;

    }

    @Subscribe
    public void onProxyInject(ProxyInitializeEvent event){
        final CloudAPIEnvironment environment = new CloudAPIEnvironment();
        environment.handleNettyConnection();
        environment.registerHandlers();
        environment.registerVelocityHandlers();
        environment.handelNettyUpdate();
    }

    @Subscribe
    public void onDisable(ProxyShutdownEvent event){
        proxyServer.getAllPlayers().forEach(player -> {
            player.disconnect(Component.text("cloudservice-shutdown"));
        });
        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInServiceDisconnect(service.getService()));
    }

}
