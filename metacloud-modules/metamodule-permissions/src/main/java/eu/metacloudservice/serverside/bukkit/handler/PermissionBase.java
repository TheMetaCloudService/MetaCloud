package eu.metacloudservice.serverside.bukkit.handler;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.ServerOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PermissionBase extends PermissibleBase {

    private final Player player;
    public PermissionBase(@Nullable Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {

        return true;
    }
}
