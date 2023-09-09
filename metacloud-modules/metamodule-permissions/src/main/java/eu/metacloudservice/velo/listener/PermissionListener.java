/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.velo.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.permission.PermissionProvider;

public class PermissionListener{

    private final PermissionProvider permissionProvider;

    public PermissionListener(PermissionProvider permissionProvider) {
        this.permissionProvider = permissionProvider;
    }

    @Subscribe
    public void handel(PermissionsSetupEvent event){
        event.setProvider(getPermissionProvider());
    }

    public PermissionProvider getPermissionProvider() {
        return permissionProvider;
    }
}
