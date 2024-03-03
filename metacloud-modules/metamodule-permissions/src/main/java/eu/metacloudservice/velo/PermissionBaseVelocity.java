/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.velo;

import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.moduleside.config.PermissionAble;

import java.util.ArrayList;

public class PermissionBaseVelocity implements PermissionProvider {
    @Override
    public PermissionFunction createFunction(PermissionSubject subject) {
        if (!(subject instanceof Player player)) return permission -> null;
        return new PermissionAbleCheck(player);
    }

    private record  PermissionAbleCheck(Player player) implements PermissionFunction {
        @Override
        public Tristate getPermissionValue(String permission) {

            ArrayList<PermissionAble> permissions = CloudPermissionAPI.getInstance().getPermissionsFromPlayer(player.getUsername());

            if (permissions.parallelStream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase("*") && permissionAble.getAble())){
                return Tristate.fromBoolean(true);
            }else if (!permissions.parallelStream().filter(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission) && permissionAble.getAble()).toList().isEmpty()){
                return Tristate.fromBoolean(true);
            }else {
                return Tristate.fromBoolean(false);
            }

        }
    }
}
