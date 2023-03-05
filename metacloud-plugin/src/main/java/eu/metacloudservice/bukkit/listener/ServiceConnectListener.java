package eu.metacloudservice.bukkit.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.webserver.dummys.Addresses;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;


public class ServiceConnectListener implements Listener {

    @EventHandler
    public void handle(PlayerLoginEvent event){
        Messages messages = CloudAPI.getInstance().getMessages();
        Addresses addressesConfig = (Addresses) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/general/addresses"), Addresses.class);
        if (addressesConfig.getWhitelist().stream().noneMatch(s -> s.equalsIgnoreCase(event.getAddress().getHostAddress().toString())) || !CloudAPI.getInstance().getPlayerPool().playerIsNotNull(event.getPlayer().getName())){
            event.getPlayer().kickPlayer(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickOnlyProxyJoin()).replace("&", "§"));
        }
    }
    @EventHandler
    public void handle(ServerListPingEvent event){
        Addresses addressesConfig = (Addresses) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/general/addresses"), Addresses.class);
        if (addressesConfig.getWhitelist().stream().noneMatch(s -> s.equalsIgnoreCase(event.getAddress().getHostAddress()))){
            event.setMaxPlayers(0);
            event.setServerIcon(null);
        }
    }

}
