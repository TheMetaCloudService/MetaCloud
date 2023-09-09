package eu.metacloudservice.bukkit.listener;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ReloadBlocker implements Listener {


    @EventHandler
    public void handle(PlayerCommandPreprocessEvent event){
        String message = event.getMessage();
        Player player = event.getPlayer();
        if (message.equalsIgnoreCase("/rl") ||
                message.equalsIgnoreCase("/reload") ||
                message.equalsIgnoreCase("/rl confirm") ||
                message.equalsIgnoreCase("/reload confirm")){

            if (player.hasPermission("bukkit.command.reload")) {
                event.setCancelled(true);
                player.sendMessage("§cCloud-Servers cannot be reloaded");
            }else {
                player.sendMessage("§cno permission to do that!");
            }
        }
    }
}
