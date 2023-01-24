package eu.themetacloudservice.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CloudPlayerHandler implements Listener {


    @EventHandler
    public void handelConnection(PlayerJoinEvent event){
        Player player = event.getPlayer();
    }
}
