package eu.metacloudservice.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.config.Tablist;
import eu.metacloudservice.velocity.VeloCityBootstrap;
import net.kyori.adventure.text.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TablistListener {

    private final ProxyServer proxyServer;


    public TablistListener(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
        proxyServer.getScheduler().buildTask(VeloCityBootstrap.getInstance(), () -> {
            proxyServer.getAllPlayers().forEach(TablistListener::sendTab);
        }).repeat(2, TimeUnit.SECONDS).schedule();
    }

    @Subscribe
    public void hanndle(PostLoginEvent event){
        proxyServer.getAllPlayers().forEach(TablistListener::sendTab);
    }
    @Subscribe
    public void handle(ServerConnectedEvent event){
        proxyServer.getAllPlayers().forEach(TablistListener::sendTab);
    }

    private static void sendTab(Player player) {

        if (VeloCityBootstrap.getInstance().configuration == null) {
            return;
        } else {

            if (!VeloCityBootstrap.getInstance().configuration.isTabEnabled()) {
                return;
            } else {
                try {
                    Tablist tab = VeloCityBootstrap.getInstance().configuration.getTablist().get(VeloCityBootstrap.getInstance().tabCount);
                    String[] config = readConfigs(tab, player);

                    player.getTabList().setHeaderAndFooter(Component.text(config[0]), Component.text(config[1]));

                } catch (Exception ignored) {
                }
            }

        }


    }
    private static String[] readConfigs(Tablist tablist, Player player) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        VeloCityBootstrap bungeeBootstrap = VeloCityBootstrap.getInstance();
        CloudAPI cloudAPI = CloudAPI.getInstance();

        String rawHeader = tablist.getHeader();
        String rawFooter = tablist.getFooter();
        String playerName = player.getUsername();
        String playerUuid = player.getUniqueId().toString();
        String serviceName = player.getCurrentServer() == null ? "" : player.getCurrentServer().get().getServerInfo().getName();
        String proxyName = bungeeBootstrap.getLiveService().getService();
        String proxyNode = bungeeBootstrap.getLiveService().getRunningNode();
        String serviceGroupName = cloudAPI.getPlayerPool().getPlayer(playerName).getServer().getGroup().getGroup();
        String serviceNode = cloudAPI.getPlayerPool().getPlayer(playerName).getServer().getGroup().getStorage().getRunningNode();
        String proxyGroupName = bungeeBootstrap.getLiveService().getGroup();
        String version =  Driver.getInstance().getMessageStorage().version;
        int onlinePlayers = cloudAPI.getPlayerPool().getPlayers().size();
        int maxPlayers = bungeeBootstrap.group.getMaxPlayers();
        int playerPing = Integer.parseInt(String.valueOf(player.getPing()));
        String time = dtf.format(LocalDateTime.now());


        String footer = rawFooter.replace("&", "§")
                .replace("%service_name%", serviceName)
                .replace("%time%", time)
                .replace("%proxy_node%", proxyNode)
                .replace("%cloud_version%",version)
                .replace("%service_node%", serviceNode)
                .replace("%service_group_name%", serviceGroupName)
                .replace("%proxy_name%", proxyName)
                .replace("%online_players%", "" + onlinePlayers)
                .replace("%max_players%", "" + maxPlayers)
                .replace("%player_ping%", "" + playerPing)
                .replace("%player_name%", playerName)
                .replace("%player_uuid%", playerUuid)
                .replace("%proxy_group_name%", proxyGroupName);

        String header = rawHeader.replace("&", "§")
                .replace("%service_name%", serviceName)
                .replace("%time%", time)
                .replace("%proxy_node%", proxyNode)
                .replace("%cloud_version%",version)
                .replace("%service_node%", serviceNode)
                .replace("%service_group_name%", serviceGroupName)
                .replace("%proxy_name%", proxyName)
                .replace("%online_players%", "" + onlinePlayers)
                .replace("%max_players%", "" + maxPlayers)
                .replace("%player_ping%", "" + playerPing)
                .replace("%player_name%", playerName)
                .replace("%player_uuid%", playerUuid)
                .replace("%proxy_group_name%", proxyGroupName);

        return new String[]{header, footer};
    }


}
