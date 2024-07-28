package eu.metacloudservice.bukkit.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.bukkit.BukkitBootstrap;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.webserver.dummys.Addresses;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;


public class ServiceConnectListener implements Listener {


    @EventHandler
    public void handlePermission(PlayerLoginEvent event){
        final Group group = CloudAPI.getInstance().getGroupPool().getGroup(BukkitBootstrap.service.getGroup());
        if (group.isMaintenance() && !event.getPlayer().hasPermission("metacloud.bypass.connection.maintenance")){
            event.getPlayer().kickPlayer(CloudAPI.getInstance().getMessages().getMessages().get("connectingGroupMaintenance").replace("&", "§").replace("%PREFIX%", CloudAPI.getInstance().getMessages().getMessages().get("prefix").replace("&", "§")));
        }else if (!event.getPlayer().hasPermission(group.getPermission())){
            event.getPlayer().kickPlayer(CloudAPI.getInstance().getMessages().getMessages().get("noPermsToJoinTheService").replace("&", "§").replace("%PREFIX%", CloudAPI.getInstance().getMessages().getMessages().get("prefix").replace("&", "§")));
        }
    }

    @EventHandler
    public void handle(PlayerLoginEvent event){
        final Messages messages = CloudAPI.getInstance().getMessages();
        final Addresses addressesConfig = (Addresses) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/default/addresses"), Addresses.class);
        if (addressesConfig.getWhitelist().stream().noneMatch(s -> s.equalsIgnoreCase(event.getAddress().getHostAddress().toString())) || !CloudAPI.getInstance().getPlayerPool().playerIsNotNull(event.getPlayer().getName())){
            event.getPlayer().kickPlayer(messages.getMessages().get("kickOnlyProxyJoin").replace("&", "§"));
        }
    }
    @EventHandler
    public void handle(ServerListPingEvent event){
        final Addresses addressesConfig = (Addresses) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/default/addresses"), Addresses.class);
        if (addressesConfig.getWhitelist().stream().noneMatch(s -> s.equalsIgnoreCase(event.getAddress().getHostAddress()))){
            event.setMaxPlayers(0);
            event.setServerIcon(null);
        }
    }
}
