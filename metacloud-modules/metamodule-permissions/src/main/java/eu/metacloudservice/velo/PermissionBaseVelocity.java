/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.general.base;

import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;

public class PermissionBaseVelocity implements PermissionProvider {
    @Override
    public PermissionFunction createFunction(PermissionSubject subject) {
        if (!(subject instanceof Player player)) return permission -> null;
        return new PermissionAbleCheck(player);
    }

    private record  PermissionAbleCheck(Player player) implements PermissionFunction {
        @Override
        public Tristate getPermissionValue(String permission) {
            return Tristate.fromBoolean(true);
        }
    }
}
