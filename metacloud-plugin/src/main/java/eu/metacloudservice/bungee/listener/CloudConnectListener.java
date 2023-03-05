package eu.metacloudservice.bungee.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerConnect;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerDisconnect;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerSwitchService;
import eu.metacloudservice.pool.service.entrys.CloudService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;

public class CloudConnectListener implements Listener {

    private final ArrayList<ProxiedPlayer> connected;

    public CloudConnectListener() {
        connected = new ArrayList<>();
    }

    @EventHandler
    public void  handle(final PostLoginEvent event){

        boolean bypassMaintenance = event.getPlayer().hasPermission("metacloud.connection.maintenance");
        boolean bypassFullNetwork = event.getPlayer().hasPermission("metacloud.connection.full");
        if (CloudAPI.getInstance().getWhitelist().contains(event.getPlayer().getName())){
            bypassFullNetwork = true;
            bypassMaintenance = true;
        }
        LiveService service = (LiveService)(new ConfigDriver("./CLOUDSERVICE.json")).read(LiveService.class);
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInPlayerConnect(event.getPlayer().getName(), service.getService()));
        Group group = CloudAPI.getInstance().getGroups().parallelStream().filter(group1 -> group1.getGroup().equalsIgnoreCase(service.getGroup())).findFirst().get();
        Messages messages = CloudAPI.getInstance().getMessages();

        if (group.isMaintenance() && !bypassMaintenance) {
            event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNetworkIsMaintenance()).replace("&", "§"));
        }else if (CloudAPI.getInstance().getPlayerPool().getPlayers().size() >= group.getMaxPlayers() && !bypassFullNetwork) {
            event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNetworkIsFull()).replace("&", "§"));
        }else if (BungeeBootstrap.getInstance().getLobby(event.getPlayer()) == null){
            event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNoFallback()).replace("&", "§"));
        }
    }



    @EventHandler
    public void handle(ServerConnectEvent event) {
        if (event.isCancelled()) return;
        if (event.getPlayer().getServer() == null){
            CloudService target = BungeeBootstrap.getInstance().getLobby(event.getPlayer());
            connected.add(event.getPlayer());
            if (target == null){
                Messages messages = CloudAPI.getInstance().getMessages();
                event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNoFallback()).replace("&", "§"));
                event.setCancelled(true);
            }else {
                event.setTarget(ProxyServer.getInstance().getServerInfo(target.getName()));
                event.setCancelled(false);
            }
        }else {
            if (event.getTarget().getName().equalsIgnoreCase("lobby")){
                CloudService target = BungeeBootstrap.getInstance().getLobby(event.getPlayer());
                if (target == null){
                    Messages messages = CloudAPI.getInstance().getMessages();
                    event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNoFallback()).replace("&", "§"));
                    event.setCancelled(true);
                }else {
                    event.setTarget(ProxyServer.getInstance().getServerInfo(target.getName()));
                    event.setCancelled(false);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(final PlayerDisconnectEvent event) {
        connected.remove(event.getPlayer());
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInPlayerDisconnect(event.getPlayer().getName()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void handle(final ServerSwitchEvent event){
        if (event.getFrom() == null && event.getPlayer().getServer() == null){
            if (CloudAPI.getInstance().getPlayerPool().playerIsNotNull(event.getPlayer().getName())) {
                CloudAPI.getInstance().sendPacketSynchronized(new PacketInPlayerSwitchService(event.getPlayer().getName(), CloudAPI.getInstance().getPlayerPool().getPlayer(event.getPlayer().getName()).getServer().getName().replace("null", "")));
            }
        }else if (CloudAPI.getInstance().getPlayerPool().playerIsNotNull(event.getPlayer().getName())) {
            CloudAPI.getInstance().sendPacketSynchronized(new PacketInPlayerSwitchService(event.getPlayer().getName(), event.getPlayer().getServer().getInfo().getName()));
        }
    }


    @EventHandler
    public void handle(final ServerKickEvent event) {
        CloudService target = BungeeBootstrap.getInstance().getLobby(event.getPlayer());
        if (target != null){
            event.setCancelServer(ProxyServer.getInstance().getServerInfo(target.getName()));
            event.setCancelled(true);
        }
    }

}
