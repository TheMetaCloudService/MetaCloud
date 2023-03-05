package eu.metacloudservice.bungee.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.Tablist;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class TabListListener implements Listener {

    @EventHandler
    public void handelServerConnection(ServerConnectedEvent event){
        ProxyServer.getInstance().getPlayers().forEach(TabListListener::sendTab);

    }

    @EventHandler
    public void login(PostLoginEvent event){
        ProxyServer.getInstance().getScheduler().schedule(BungeeBootstrap.getInstance(), () -> sendTab(event.getPlayer()), 1, 2, TimeUnit.SECONDS);
    }

    @EventHandler
    public void handelServerConnection(ServerSwitchEvent event){
        ProxyServer.getInstance().getPlayers().forEach(TabListListener::sendTab);
    }

    private static void sendTab(ProxiedPlayer player){
        try {
            Tablist tablist = BungeeBootstrap.getInstance().configuration.getTablist().get(BungeeBootstrap.getInstance().tabCount);
            player.setTabHeader(TextComponent.fromLegacyText(Driver.getInstance().getMessageStorage().base64ToUTF8(tablist.getHeader())
                            .replace("&", "§")
                            .replace("%service_name%", player.getServer().getInfo().getName())
                            .replace("%proxy_name%", BungeeBootstrap.getInstance().getLiveService().getService())
                            .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayers().size())
                            .replace("%max_players%", ""+ BungeeBootstrap.getInstance().group.getMaxPlayers())
                            .replace("%player_ping%", String.valueOf(player.getPing()))
                            .replace("%player_name%", player.getName())
                            .replace("%proxy_group_name%", BungeeBootstrap.getInstance().getLiveService().getGroup())
                            .replace("%service_group_name%", CloudAPI.getInstance().getPlayerPool().getPlayer(player.getName()).getServer().getGroup().getGroup())),
                    TextComponent.fromLegacyText(Driver.getInstance().getMessageStorage().base64ToUTF8(tablist.getFooter()).replace("&", "§")
                            .replace("%service_name%", player.getServer().getInfo().getName())
                            .replace("%proxy_name%", BungeeBootstrap.getInstance().getLiveService().getService())
                            .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayers().size())
                            .replace("%max_players%", ""+ BungeeBootstrap.getInstance().group.getMaxPlayers())
                            .replace("%player_ping%", String.valueOf(player.getPing()))
                            .replace("%player_name%", player.getName())
                            .replace("%proxy_group_name%", BungeeBootstrap.getInstance().getLiveService().getGroup())
                            .replace("%service_group_name%", CloudAPI.getInstance().getPlayerPool().getPlayer(player.getName()).getServer().getGroup().getGroup())));

        }catch (Exception e){}
    }
}
