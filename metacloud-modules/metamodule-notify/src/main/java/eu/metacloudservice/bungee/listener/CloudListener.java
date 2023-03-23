package eu.metacloudservice.bungee.listener;

import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.EventHandler;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.listeners.*;
import net.md_5.bungee.api.ProxyServer;

public class CloudListener implements ICloudListener {


    @EventHandler
    public void handle(CloudProxyDisconnectedEvent event){

        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(configuration.getProxiedServiceDiconnected()).replace("&", "§").replace("%service_name%", event.getName()));
            }
        });
    }

    @EventHandler
    public void handle(CloudProxyConnectedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(configuration.getProxiedServiceConnected()).replace("&", "§")
                        .replace("%service_name%", event.getName()).replace("%node_name%", event.getNode()));
            }
        });
    }

    @EventHandler
    public void handle(CloudProxyPreparedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(configuration.getProxiedServicePrepared()).replace("&", "§")
                        .replace("%service_name%", event.getName()).replace("%node_name%", event.getNode()));
            }
        });
    }

    @EventHandler
    public void handle(CloudServiceConnectedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")) {
                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(configuration.getServiceConnected()).replace("&", "§")
                        .replace("%service_name%", event.getName()).replace("%node_name%", event.getNode()));
            }
        });
    }

    @EventHandler
    public void handle(CloudServiceDisconnectedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {

            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(configuration.getServiceDiconnected()).replace("&", "§")
                        .replace("%service_name%", event.getName()));

            }
        });
    }

    @EventHandler
    public void handle(CloudServicePreparedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(configuration.getServicePrepared()).replace("&", "§")
                        .replace("%service_name%", event.getName()).replace("%node_name%", event.getNode()));
            }

        });
    }
}
