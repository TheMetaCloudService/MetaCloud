package eu.metacloudservice.bungee.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.config.Motd;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class MotdListener  implements Listener {



    @EventHandler(priority = EventPriority.HIGHEST)
    public void handelPings(ProxyPingEvent event){
        ServerPing ping = event.getResponse();

        if (BungeeBootstrap.getInstance().configuration == null){
            return;
        }else if (!BungeeBootstrap.getInstance().configuration.isMotdEnabled()){
            return;
        }else {

            BungeeBootstrap bungeeBootstrap = BungeeBootstrap.getInstance();
            CloudAPI cloudAPI = CloudAPI.getInstance();
            int motdIndex = bungeeBootstrap.motdCount;
            Motd motd = bungeeBootstrap.group.isMaintenance() ? bungeeBootstrap.configuration.getMaintenancen().get(motdIndex) : bungeeBootstrap.configuration.getDefaults().get(motdIndex);

            ServerPing.PlayerInfo[] playerInfos = motd.getPlayerinfos().stream()
                    .map(info -> new ServerPing.PlayerInfo(
                            Driver.getInstance().getMessageStorage().base64ToUTF8(info).replace("&", "§"),
                            UUID.randomUUID().toString()))
                    .toArray(ServerPing.PlayerInfo[]::new);

            ServerPing.Players players = new ServerPing.Players(
                    bungeeBootstrap.group.getMaxPlayers(),
                    cloudAPI.getPlayerPool().getPlayers().size(),
                    playerInfos.length > 0 ? playerInfos : null);
            String protocol = motd.getProtocol();
            String protocolString = protocol != null && !protocol.isEmpty() ?
                    Driver.getInstance().getMessageStorage().base64ToUTF8(protocol)
                            .replace("&", "§")
                            .replace("%proxy_name%", bungeeBootstrap.getLiveService().getService())
                            .replace("%proxy_node%", bungeeBootstrap.getLiveService().getRunningNode())
                            .replace("%online_players%", "" + cloudAPI.getPlayerPool().getPlayers().size())
                            .replace("%max_players%", "" + bungeeBootstrap.group.getMaxPlayers()) :
                    "§7" + cloudAPI.getPlayerPool().getPlayers().size() + "/" + bungeeBootstrap.group.getMaxPlayers();

            ServerPing.Protocol serverProtocol = new ServerPing.Protocol(protocolString, ping.getVersion().getProtocol() - 1);
            String firstLine = Driver.getInstance().getMessageStorage().base64ToUTF8(motd.getFirstline())
                    .replace("&", "§")
                    .replace("%proxy_name%", bungeeBootstrap.getLiveService().getService())
                    .replace("%proxy_node%", bungeeBootstrap.getLiveService().getRunningNode())
                    .replace("%proxy_group%", bungeeBootstrap.getLiveService().getGroup())
                    .replace("%online_players%", "" + cloudAPI.getPlayerPool().getPlayers().size())
                    .replace("%max_players%", "" + bungeeBootstrap.group.getMaxPlayers());

            String secondLine = Driver.getInstance().getMessageStorage().base64ToUTF8(motd.getSecondline())
                    .replace("&", "§")
                    .replace("%proxy_name%", bungeeBootstrap.getLiveService().getService())
                    .replace("%proxy_node%", bungeeBootstrap.getLiveService().getRunningNode())
                    .replace("%proxy_group%", bungeeBootstrap.getLiveService().getGroup())
                    .replace("%online_players%", "" + cloudAPI.getPlayerPool().getPlayers().size())
                    .replace("%max_players%", "" + bungeeBootstrap.group.getMaxPlayers());


            String description = firstLine + "\n" + secondLine;

            ping.setDescription(description);
            ping.setVersion(serverProtocol);
            ping.setPlayers(players);


            event.setResponse(ping);

        }

    }


}
