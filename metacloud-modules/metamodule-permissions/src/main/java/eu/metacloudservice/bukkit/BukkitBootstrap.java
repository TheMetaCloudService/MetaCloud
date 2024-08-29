/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bukkit;

import eu.metacloudservice.api.CloudPermissionAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class BukkitBootstrap extends JavaPlugin  implements Listener {


    @Override
    public void onLoad() {
        new CloudPermissionAPI();
    }

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void handle(PlayerJoinEvent event){
        try {
            Field field = reflectCraftClazz(".entity.CraftHumanEntity").getDeclaredField("perm");
            field.setAccessible(true);
            field.set(event.getPlayer(), new PermissionBaseBukkit(event.getPlayer()));
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.fillInStackTrace();
        }

    }


    private Class<?> reflectCraftClazz(String suffix) {
        try {
            // INFO: Seems like they removed the version from the package name? (https://forums.papermc.io/threads/important-dev-psa-future-removal-of-cb-package-relocation.1106/)
            return Class.forName("org.bukkit.craftbukkit" + suffix);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                return Class.forName("org.bukkit.craftbukkit" + suffix);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
