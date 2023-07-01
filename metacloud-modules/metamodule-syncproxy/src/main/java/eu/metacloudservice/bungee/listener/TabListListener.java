package eu.metacloudservice.bungee.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.config.Tablist;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TabListListener implements Listener {
    
    public TabListListener() {
        ProxyServer
                .getInstance()
                .getScheduler()
                .runAsync(BungeeBootstrap.getInstance(), () -> ProxyServer
                        .getInstance()
                        .getScheduler()
                        .schedule(BungeeBootstrap.getInstance(), () -> {ProxyServer
                                .getInstance()
                                .getPlayers()
                                .forEach(TabListListener::sendTab);}, 0, 2, TimeUnit.SECONDS));
    }

    @EventHandler
    public void onConnect(PostLoginEvent event){
        ProxyServer.getInstance().getPlayers().forEach(TabListListener::sendTab);
    }

    @EventHandler
    public void handelServerConnection(ServerConnectedEvent event){
        ProxyServer.getInstance().getPlayers().forEach(TabListListener::sendTab);

    }

    @EventHandler
    public void handelServerConnection(ServerSwitchEvent event){
        ProxyServer.getInstance().getPlayers().forEach(TabListListener::sendTab);
    }

    private static void sendTab(ProxiedPlayer player) {

        if (BungeeBootstrap.getInstance().configuration == null) {
            return;
        } else {

            if (!BungeeBootstrap.getInstance().configuration.isTabEnabled()) {
                return;
            } else {
                Tablist tab = BungeeBootstrap.getInstance().configuration.getTablist().get(BungeeBootstrap.getInstance().tabCount);
                String[] config = readConfigs(tab, player);

                player.setTabHeader(TextComponent.fromLegacyText(config[0]), TextComponent.fromLegacyText(config[1]));
            }
        }

    }
    private static String[] readConfigs(Tablist tablist, ProxiedPlayer player) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        BungeeBootstrap bungeeBootstrap = BungeeBootstrap.getInstance();
        CloudAPI cloudAPI = CloudAPI.getInstance();

        String rawHeader = Driver.getInstance().getMessageStorage().base64ToUTF8(tablist.getHeader());
        String rawFooter = Driver.getInstance().getMessageStorage().base64ToUTF8(tablist.getFooter());
        String playerName = player.getName();
        String playerUuid = player.getUUID();
        String serviceName = player.getServer().getInfo().getName();
        String proxyName = bungeeBootstrap.getLiveService().getService();
        String proxyNode = bungeeBootstrap.getLiveService().getRunningNode();
        String serviceGroupName = cloudAPI.getPlayerPool().getPlayer(playerName).getServer().getGroup().getGroup();
        String serviceNode = cloudAPI.getPlayerPool().getPlayer(playerName).getServer().getGroup().getStorage().getRunningNode();
        String proxyGroupName = bungeeBootstrap.getLiveService().getGroup();
        String version =  Driver.getInstance().getMessageStorage().version;
        int onlinePlayers = cloudAPI.getPlayerPool().getPlayers().size();
        double memory = CloudAPI.getInstance().getUsedMemory();
        double maxMemory = CloudAPI.getInstance().getUsedMemory();
        int maxPlayers = bungeeBootstrap.group.getMaxPlayers();
        int playerPing = player.getPing();
        String time = dtf.format(LocalDateTime.now());
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(CloudAPI.getInstance().getPlayerPool().getPlayer(playerName).getCurrentPlayTime());
        String playtime =  (cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE));


        String footer = rawFooter.replace("&", "§")
                .replace("%service_name%", serviceName)
                .replace("%time%", time)
                .replace("%memory%", ""+memory)
                .replace("%max_memory%", ""+maxMemory)
                .replace("%proxy_node%", proxyNode)
                .replace("%cloud_version%",version)
                .replace("%service_node%", serviceNode)
                .replace("%service_group_name%", serviceGroupName)
                .replace("%proxy_name%", proxyName)
                .replace("%online_players%", "" + onlinePlayers)
                .replace("%max_players%", "" + maxPlayers)
                .replace("%player_ping%", "" + playerPing)
                .replace("%player_name%", playerName)
                .replace("%player_playtime%", playtime)
                .replace("%player_uuid%", playerUuid)
                .replace("%proxy_group_name%", proxyGroupName);

        String header = rawHeader.replace("&", "§")
                .replace("%service_name%", serviceName)
                .replace("%time%", time)
                .replace("%memory%", ""+memory)
                .replace("%max_memory%", ""+maxMemory)
                .replace("%proxy_node%", proxyNode)
                .replace("%cloud_version%",version)
                .replace("%service_node%", serviceNode)
                .replace("%service_group_name%", serviceGroupName)
                .replace("%proxy_name%", proxyName)
                .replace("%online_players%", "" + onlinePlayers)
                .replace("%max_players%", "" + maxPlayers)
                .replace("%player_ping%", "" + playerPing)
                .replace("%player_playtime%", playtime)
                .replace("%player_name%", playerName)
                .replace("%player_uuid%", playerUuid)
                .replace("%proxy_group_name%", proxyGroupName);

        return new String[]{header, footer};


    }

}
