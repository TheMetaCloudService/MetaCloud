/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.events;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.serverside.bukkit.SignBootstrap;
import eu.metacloudservice.serverside.bukkit.entry.CloudSign;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvent implements Listener {
    @EventHandler
    public void handleInterec(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block targetBlock = event.getClickedBlock();
            if (targetBlock.getState() instanceof Sign){
                CloudSign sign = SignBootstrap.signDriver.getSignByLocation(targetBlock.getLocation());
                if (sign != null){
                    if (!sign.getService().isEmpty()){
                        CloudAPI.getInstance().getPlayerPool().getPlayer(event.getPlayer().getName()).connect(CloudAPI.getInstance().getServicePool().getService(sign.getService()));
                    }
                }
            }
        }
    }
}
