package eu.metacloudservice.bungee.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.config.Motd;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class MotdListener  implements Listener {



    @EventHandler
    public void handelPings(ProxyPingEvent event){
        ServerPing ping = event.getResponse();
        ServerPing.Players players = ping.getPlayers();
           if (BungeeBootstrap.getInstance().group.isMaintenance()){
            Motd motd = BungeeBootstrap.getInstance().configuration.getMaintenancen().get(BungeeBootstrap.getInstance().motdCount);
            if (!motd.getPlayerinfos().isEmpty()){

                String array[] = new String[motd.getPlayerinfos().size()];
                for(int j =0;j<motd.getPlayerinfos().size();j++){
                    array[j] = motd.getPlayerinfos().get(j);
                }
                ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[array.length];
                for (int i = 0; i < playerInfo.length; i++) {
                    playerInfo[i] = new ServerPing.PlayerInfo(
                            Driver.getInstance().getMessageStorage().base64ToUTF8(array[i]).replace("&", "§"),
                            UUID.randomUUID().toString());
                }

                ping.setPlayers( new ServerPing.Players(BungeeBootstrap.getInstance().group.getMaxPlayers(), CloudAPI.getInstance().getPlayerPool().getPlayers().size(), playerInfo));
            }else{
                ping.setPlayers( new ServerPing.Players(BungeeBootstrap.getInstance().group.getMaxPlayers(),  CloudAPI.getInstance().getPlayerPool().getPlayers().size(), null));
            }
            if (motd.getProtocol() != null && !motd.getProtocol().equalsIgnoreCase("")) {
                ping.setVersion(new ServerPing.Protocol(Driver.getInstance().getMessageStorage().base64ToUTF8(motd.getProtocol())
                        .replace("&", "§")
                        .replace("%proxy_name%", BungeeBootstrap.getInstance().getLiveService().getService())
                        .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayers().size())
                        .replace("%max_players%", ""+ BungeeBootstrap.getInstance().group.getMaxPlayers()), ping.getVersion().getProtocol() -1));
            }else {
                ping.setVersion(new ServerPing.Protocol("§7" + CloudAPI.getInstance().getPlayerPool().getPlayers().size() + "/" + BungeeBootstrap.getInstance().group.getMaxPlayers(), ping.getVersion().getProtocol() -1));
            }


            ping.setDescription( Driver.getInstance().getMessageStorage().base64ToUTF8(motd.getFirstline())
                    .replace("&", "§")
                    .replace("%proxy_name%", BungeeBootstrap.getInstance().getLiveService().getService())
                    .replace("%proxy_group%", BungeeBootstrap.getInstance().getLiveService().getGroup())
                     .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayers().size())
                    .replace("%max_players%", ""+BungeeBootstrap.getInstance().group.getMaxPlayers())
                    + "\n" +
                    Driver.getInstance().getMessageStorage().base64ToUTF8(motd.getSecondline())
                            .replace("&", "§")
                            .replace("%proxy_name%", BungeeBootstrap.getInstance().getLiveService().getService())
                            .replace("%proxy_group%", BungeeBootstrap.getInstance().getLiveService().getGroup())
                            .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayers().size())
                            .replace("%max_players%", ""+BungeeBootstrap.getInstance().group.getMaxPlayers()));

            event.setResponse(ping);
        }else {
            Motd motd = BungeeBootstrap.getInstance().configuration.getDefaults().get(BungeeBootstrap.getInstance().motdCount);
            if (!motd.getPlayerinfos().isEmpty()){

                String array[] = new String[motd.getPlayerinfos().size()];
                for(int j =0;j<motd.getPlayerinfos().size();j++){
                    array[j] = motd.getPlayerinfos().get(j);
                }
                ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[array.length];
                for (int i = 0; i < playerInfo.length; i++) {
                    playerInfo[i] = new ServerPing.PlayerInfo(
                            Driver.getInstance().getMessageStorage().base64ToUTF8(array[i]).replace("&", "§"),
                            UUID.randomUUID().toString());
                }

                ping.setPlayers( new ServerPing.Players(BungeeBootstrap.getInstance().group.getMaxPlayers(), CloudAPI.getInstance().getPlayerPool().getPlayers().size(), playerInfo));
            }else{
                ping.setPlayers( new ServerPing.Players(BungeeBootstrap.getInstance().group.getMaxPlayers(),  CloudAPI.getInstance().getPlayerPool().getPlayers().size(), null));
            }
            if (motd.getProtocol() != null && !motd.getProtocol().equalsIgnoreCase("")) {
                ping.setVersion(new ServerPing.Protocol(Driver.getInstance().getMessageStorage().base64ToUTF8(motd.getProtocol())
                        .replace("&", "§")
                        .replace("%proxy_name%", BungeeBootstrap.getInstance().getLiveService().getService())
                        .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayers().size())
                        .replace("%max_players%", ""+ BungeeBootstrap.getInstance().group.getMaxPlayers()), ping.getVersion().getProtocol() -1));
            }else {
                ping.setVersion(new ServerPing.Protocol("§7" + CloudAPI.getInstance().getPlayerPool().getPlayers().size() + "/" +BungeeBootstrap.getInstance(). group.getMaxPlayers(), ping.getVersion().getProtocol() -1));
            }


            ping.setDescription( Driver.getInstance().getMessageStorage().base64ToUTF8(motd.getFirstline())
                    .replace("&", "§")
                    .replace("%proxy_name%", BungeeBootstrap.getInstance().getLiveService().getService())
                    .replace("%proxy_group%", BungeeBootstrap.getInstance().getLiveService().getGroup())
                    .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayers().size())
                    .replace("%max_players%", ""+BungeeBootstrap.getInstance().group.getMaxPlayers())
                    + "\n" +
                    Driver.getInstance().getMessageStorage().base64ToUTF8(motd.getSecondline())
                            .replace("&", "§")
                            .replace("%proxy_name%", BungeeBootstrap.getInstance().getLiveService().getService())
                            .replace("%proxy_group%", BungeeBootstrap.getInstance().getLiveService().getGroup())
                            .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayers().size())
                            .replace("%max_players%", ""+BungeeBootstrap.getInstance().group.getMaxPlayers()));

            event.setResponse(ping);
        }

    }
}
