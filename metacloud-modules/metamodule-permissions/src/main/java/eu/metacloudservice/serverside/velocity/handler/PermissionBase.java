package eu.metacloudservice.serverside.velocity.handler;

import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;

public class PermissionBase implements PermissionProvider{
    @Override
    public PermissionFunction createFunction(PermissionSubject permissionSubject) {
        if (!(permissionSubject instanceof Player)) return permission -> null;
        return new check((Player) permissionSubject);
    }

    private record check(Player player) implements PermissionFunction {

        @Override
        public Tristate getPermissionValue(String permission) {
            return null;
        }
    }

}
