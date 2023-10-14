/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bungee.listener;

import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.moduleside.config.PermissionAble;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class PermissionListener implements Listener {


     @EventHandler
    public void handle(PermissionCheckEvent event){
         if (event.getSender() instanceof ProxiedPlayer){
             ArrayList<PermissionAble> permissions = CloudPermissionAPI.getInstance().getPermissionsFormPlayer(event.getSender().getName());

             if (permissions.parallelStream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase("*") && permissionAble.getAble())){
                 event.setHasPermission(true);
             }else if (!permissions.parallelStream().filter(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(event.getPermission()) && permissionAble.getAble()).toList().isEmpty()){
                 event.setHasPermission(true);
             }else {
                 event.setHasPermission(false);
             }
         }
     }
}
