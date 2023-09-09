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
    public void onEnable() {
        new CloudPermissionAPI();
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
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            return Class.forName("org.bukkit.craftbukkit." + version + suffix);
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
