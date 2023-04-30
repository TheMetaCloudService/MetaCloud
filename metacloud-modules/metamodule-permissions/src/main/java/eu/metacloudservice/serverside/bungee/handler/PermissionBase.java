package eu.metacloudservice.serverside.bungee.handler;

import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PermissionBase implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void handel(PermissionCheckEvent event){
        String permission = event.getPermission();
        event.setHasPermission(true);
    }
}
