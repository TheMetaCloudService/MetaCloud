package eu.metacloudservice.bukkit.listener;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;

public class ReloadBlocker implements Listener {


    private final String[] BLOCK_COMMANDS = {"/rl", "/reload", "/rl confirm", "/reload confirm"};

    @EventHandler
    public void handle(PlayerCommandPreprocessEvent event){
       final String message = event.getMessage();
       final Player player = event.getPlayer();
       if (Arrays.stream(this.BLOCK_COMMANDS).anyMatch(s -> s.equalsIgnoreCase(message))){
           if (player.hasPermission("bukkit.command.reload")) {
               event.setCancelled(true);
               player.sendMessage("§cCloud-Servers cannot be reloaded");
           }else {
               player.sendMessage("§cno permission to do that!");
           }
       }
    }
}
