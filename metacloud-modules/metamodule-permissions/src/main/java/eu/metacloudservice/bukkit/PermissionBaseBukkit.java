/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bukkit;

import com.velocitypowered.api.permission.Tristate;
import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.moduleside.config.PermissionAble;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.ServerOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

public class PermissionBaseBukkit extends PermissibleBase {

    private final Player player;

    public PermissionBaseBukkit(@NotNull Player player) {
        super(player);
        this.player = player;
    }


    @Override
    public boolean hasPermission(@NotNull String inName) {
        if (player.isOp()) return true;
        ArrayList<PermissionAble> permissions = CloudPermissionAPI.getInstance().getPermissionsFromPlayer(player.getName());
        if (permissions.parallelStream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase("*") && permissionAble.getAble())){
            return true;
        }else if (!permissions.parallelStream().filter(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(inName) && permissionAble.getAble()).toList().isEmpty()){
            return true;
        }else {
            return false;
        }
    }
}

