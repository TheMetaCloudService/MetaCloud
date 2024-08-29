package eu.metacloudservice.bungee.listener;

import eu.metacloudservice.api.Translator;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.services.*;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ProxyServer;

public class CloudListener implements ICloudListener {


    @Subscribe
    public void handle(CloudProxyDisconnectedEvent event){

        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                BungeeBootstrap.getInstance().audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(new Translator().translate(configuration.getProxiedServiceDiconnected().replace("%service_name%", event.getName()))));
            }
        });
    }

    @Subscribe
    public void handle(CloudProxyConnectedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                BungeeBootstrap.getInstance().audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(new Translator().translate(configuration.getProxiedServiceConnected().replace("%service_name%", event.getName()).replace("%service_node%", event.getNode()))));
            }
        });
    }

    @Subscribe
    public void handle(CloudProxyPreparedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){

                BungeeBootstrap.getInstance().audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(new Translator().translate(configuration.getProxiedServicePrepared().replace("%service_name%", event.getName()).replace("%service_node%", event.getNode()))));
            }
        });
    }

    @Subscribe
    public void handle(CloudServiceConnectedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")) {

                BungeeBootstrap.getInstance().audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(new Translator().translate(configuration.getServiceConnected().replace("%service_name%", event.getName()).replace("%service_node%", event.getNode()))));

            }
        });
    }

    @Subscribe
    public void handle(CloudServiceDisconnectedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {

            if (player.hasPermission("metacloud.notify")){
                BungeeBootstrap.getInstance().audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(new Translator().translate(configuration.getServiceDiconnected().replace("%service_name%", event.getName()))));
            }
        });
    }

    @Subscribe
    public void handle(CloudServicePreparedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                BungeeBootstrap.getInstance().audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(new Translator().translate(configuration.getServicePrepared().replace("%service_name%", event.getName()).replace("%service_node%", event.getNode()))));
            }

        });
    }
    @Subscribe
    public void handle(CloudProxyLaunchEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                BungeeBootstrap.getInstance().audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(new Translator().translate(configuration.getProxiedServiceLaunch().replace("%service_name%", event.getName()).replace("%service_node%", event.getNode()))));
            }

        });
    }
    @Subscribe
    public void handle(CloudServiceLaunchEvent event){
        Configuration configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/notify/configuration"), Configuration.class);

        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.hasPermission("metacloud.notify")){
                BungeeBootstrap.getInstance().audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(new Translator().translate(configuration.getServiceLaunch().replace("%service_name%", event.getName()).replace("%service_node%", event.getNode()))));

            }

        });
    }
}
