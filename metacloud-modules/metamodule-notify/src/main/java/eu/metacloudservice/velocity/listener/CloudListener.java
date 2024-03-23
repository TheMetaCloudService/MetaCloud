/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.velocity.listener;

import com.velocitypowered.api.proxy.ProxyServer;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.services.*;
import net.kyori.adventure.text.Component;


public class CloudListener implements ICloudListener {

    private final ProxyServer proxyServer;

    public CloudListener(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void handle(CloudProxyDisconnectedEvent event){

        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);
        proxyServer.getAllPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage((Component.text(configuration.getProxiedServiceDiconnected().replace("&", "§").replace("%service_name%", event.getName()))));
            }
        });

    }

    @Subscribe
    public void handle(CloudProxyConnectedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);
        proxyServer.getAllPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Component.text((configuration.getProxiedServiceConnected()).replace("&", "§")
                        .replace("%service_name%", event.getName()).replace("%node_name%", event.getNode())));
            }
        });
    }

    @Subscribe
    public void handle(CloudProxyPreparedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);
        proxyServer.getAllPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Component.text((configuration.getProxiedServicePrepared()).replace("&", "§")
                        .replace("%service_name%", event.getName()).replace("%node_name%", event.getNode())));
            }
        });

    }

    @Subscribe
    public void handle(CloudServiceConnectedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        proxyServer.getAllPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Component.text((configuration.getServiceConnected()).replace("&", "§")
                        .replace("%service_name%", event.getName()).replace("%node_name%", event.getNode())));
            }
        });

    }

    @Subscribe
    public void handle(CloudServiceDisconnectedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);


        proxyServer.getAllPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Component.text((configuration.getServiceDiconnected()).replace("&", "§").replace("%service_name%", event.getName())));
            }
        });


    }

    @Subscribe
    public void handle(CloudServicePreparedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        proxyServer.getAllPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Component.text(configuration.getServicePrepared().replace("&", "§")
                        .replace("%service_name%", event.getName()).replace("%node_name%", event.getNode())));
            }
        });
    }

    @Subscribe
    public void handle(CloudProxyLaunchEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        proxyServer.getAllPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Component.text((configuration.getProxiedServiceLaunch()).replace("&", "§")
                        .replace("%service_name%", event.getName()).replace("%node_name%", event.getNode())));
            }
        });
    }

    @Subscribe
    public void handle(CloudServiceLaunchEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        proxyServer.getAllPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Component.text((configuration.getServiceLaunch()).replace("&", "§")
                        .replace("%service_name%", event.getName()).replace("%node_name%", event.getNode())));
            }
        });
    }

}
