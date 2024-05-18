package eu.metacloudservice.bungee.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.config.Motd;
import eu.metacloudservice.moduleside.converter.IconConverter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

public class MotdListener  implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void handelPings(ProxyPingEvent event) throws IOException {
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
            String icon = motd.getIcon();
            double memory = CloudAPI.getInstance().getUsedMemory();
            double maxMemory = CloudAPI.getInstance().getUsedMemory();


            ServerPing.PlayerInfo[] playerInfos = motd.getPlayerinfos().stream()
                    .map(info -> new ServerPing.PlayerInfo(
                                  info.replace("&", "§"),
                            UUID.randomUUID().toString()))
                    .toArray(ServerPing.PlayerInfo[]::new);
            ServerPing.Players players = new ServerPing.Players(
                    bungeeBootstrap.group.getMaxPlayers(),
                    cloudAPI.getPlayerPool().getPlayers().size(),
                    playerInfos.length > 0 ? playerInfos : null);
            String protocol = motd.getProtocol();
            String protocolString = protocol != null && !protocol.isEmpty() ?
                  protocol
                            .replace("&", "§")
                            .replace("%proxy_name%", bungeeBootstrap.getLiveService().getService())
                            .replace("%proxy_node%", bungeeBootstrap.getLiveService().getRunningNode())
                           .replace("%proxy_group%", bungeeBootstrap.getLiveService().getGroup())
                           .replace("%memory%", ""+memory)
                           .replace("%max_memory%", ""+maxMemory)
                            .replace("%online_players%", "" + cloudAPI.getPlayerPool().getPlayers().size())
                            .replace("%max_players%", "" + bungeeBootstrap.group.getMaxPlayers()):

                    "§7" + cloudAPI.getPlayerPool().getPlayers().size() + "/" + bungeeBootstrap.group.getMaxPlayers();

            String firstLine =motd.getFirstline()
                    .replace("&", "§")
                    .replace("%proxy_name%", bungeeBootstrap.getLiveService().getService())
                    .replace("%proxy_node%", bungeeBootstrap.getLiveService().getRunningNode())
                    .replace("%proxy_group%", bungeeBootstrap.getLiveService().getGroup())
                    .replace("%memory%", ""+memory)
                    .replace("%max_memory%", ""+maxMemory)
                    .replace("%online_players%", "" + cloudAPI.getPlayerPool().getPlayers().size())
                    .replace("%max_players%", "" + bungeeBootstrap.group.getMaxPlayers());

            String secondLine = motd.getSecondline()
                    .replace("&", "§")
                    .replace("%proxy_name%", bungeeBootstrap.getLiveService().getService())
                    .replace("%proxy_node%", bungeeBootstrap.getLiveService().getRunningNode())
                    .replace("%proxy_group%", bungeeBootstrap.getLiveService().getGroup())
                    .replace("%memory%", ""+memory)
                    .replace("%max_memory%", ""+maxMemory)
                    .replace("%online_players%", "" + cloudAPI.getPlayerPool().getPlayers().size())
                    .replace("%max_players%", "" + bungeeBootstrap.group.getMaxPlayers());

            String description = firstLine + "\n" + secondLine;

            ServerPing.Protocol serverProtocol = new ServerPing.Protocol(protocolString, ping.getVersion().getProtocol() - 1);


            ping.setDescriptionComponent(BungeeComponentSerializer.get().serialize(MiniMessage.miniMessage().deserialize(BungeeBootstrap.getInstance().translator.translate(description)))[0]);
            ping.setVersion(serverProtocol);
            ping.setPlayers(players);

            if (bungeeBootstrap.iconBase.getIcons().containsKey(icon.replace(".png", ""))) {
                ByteArrayInputStream bais = new ByteArrayInputStream(new IconConverter().convertToByte(bungeeBootstrap.iconBase.getIcons().get(icon.replace(".png", ""))));
                BufferedImage bi = ImageIO.read(bais);
                bais.close();
                ping.setFavicon(Favicon.create(bi));
            }
            event.setResponse(ping);


        }

    }




}
