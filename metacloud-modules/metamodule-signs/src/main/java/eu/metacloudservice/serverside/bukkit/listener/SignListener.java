/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.serverside.bukkit.BukkitBootstrap;
import eu.metacloudservice.serverside.bukkit.signs.CloudSign;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

    @EventHandler
    public void  interact(PlayerInteractEvent event){
        if (event.getClickedBlock().getState() instanceof Sign sign){
            if (BukkitBootstrap.getInstance().getSignDriver().getSign(sign.getLocation()) != null){
                if (!BukkitBootstrap.getInstance().getSignDriver().getSign(sign.getLocation()).getServer().equals("")){
                   CloudSign cs = BukkitBootstrap.getInstance().getSignDriver().getSign(sign.getLocation());
                    CloudAPI.getInstance().getPlayerPool().getPlayer(event.getPlayer().getName()).connect(CloudAPI.getInstance().getServicePool().getService(cs.getServer()));
                }
            }

        }
    }

}
