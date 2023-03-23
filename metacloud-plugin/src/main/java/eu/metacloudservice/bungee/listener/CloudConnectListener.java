package eu.metacloudservice.bungee.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerConnect;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerDisconnect;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerSwitchService;
import eu.metacloudservice.networking.packet.Packet;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.UUID;


public class CloudConnectListener implements Listener {


    private final ArrayList<UUID> connected = new ArrayList<>();

    public ServerInfo target;

    @EventHandler(priority = - 127)
    public void handle(final ServerConnectEvent event){
        if (event.getPlayer().getServer() == null){
                target = ProxyServer.getInstance().getServerInfo(BungeeBootstrap.getInstance().getLobby(event.getPlayer()).getName());
            if (target != null){
                event.setTarget(target);
            }else event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(final PostLoginEvent event) {
        LiveService service = (LiveService)(new ConfigDriver("./CLOUDSERVICE.json")).read(LiveService.class);
        Group group = CloudAPI.getInstance().getGroups().parallelStream().filter(group1 -> group1.getGroup().equalsIgnoreCase(service.getGroup())).findFirst().get();

        this.connected.add(event.getPlayer().getUniqueId());
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInPlayerConnect(event.getPlayer().getName(), service.getService()));

        if (group.isMaintenance()
                && !ProxyServer.getInstance().getPlayer(event.getPlayer().getUniqueId()).hasPermission("metacloud.connection.maintenance")
                && !CloudAPI.getInstance().getWhitelist().contains(ProxyServer.getInstance().getPlayer(event.getPlayer().getUniqueId()).getName())){
            event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(CloudAPI.getInstance().getMessages().getKickNetworkIsMaintenance()).replace("&", "§"));

        }else if (CloudAPI.getInstance().getPlayerPool().getPlayers().size() >= group.getMaxPlayers().intValue()

                && !ProxyServer.getInstance().getPlayer(event.getPlayer().getUniqueId()).hasPermission("metacloud.connection.full")
                && !CloudAPI.getInstance().getWhitelist().contains(ProxyServer.getInstance().getPlayer(event.getPlayer().getUniqueId()).getName())){
            event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(CloudAPI.getInstance().getMessages().getKickNetworkIsFull()).replace("&", "§"));

        }else if ( ProxyServer.getInstance().getPlayer(event.getPlayer().getUniqueId()) != null
                && BungeeBootstrap.getInstance().getLobby( ProxyServer.getInstance().getPlayer(event.getPlayer().getUniqueId())) == null){
            event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(CloudAPI.getInstance().getMessages().getKickNoFallback()).replace("&", "§"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(final PlayerDisconnectEvent event) {
        if (this.connected.contains(event.getPlayer().getUniqueId())) {
            CloudAPI.getInstance().sendPacketAsynchronous(new PacketInPlayerDisconnect(event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void handle(ServerSwitchEvent event){
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInPlayerSwitchService(event.getPlayer().getName(), event.getPlayer().getServer().getInfo().getName()));
    }

    @EventHandler(priority = - 127)
    public void handle(final ServerKickEvent event) {
        target  = ProxyServer.getInstance().getServerInfo(BungeeBootstrap.getInstance().getLobby(event.getPlayer(), event.getKickedFrom().getName()).getName());
        if (target != null) {
            event.setCancelServer(target);
            event.setCancelled(true);
        } else {
            event.setCancelled(false);
            event.setCancelServer(null);
            event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(CloudAPI.getInstance().getMessages().getKickNoFallback()).replace("&", "§"));
        }

    }

}