/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.PluginDriver;
import eu.metacloudservice.api.SignsAPI;
import eu.metacloudservice.serverside.bukkit.commands.Command;
import eu.metacloudservice.serverside.bukkit.drivers.SignDriver;
import eu.metacloudservice.serverside.bukkit.drivers.SignUpdaterTask;
import eu.metacloudservice.serverside.bukkit.entry.CloudSign;
import eu.metacloudservice.serverside.bukkit.events.CloudEventHandler;
import eu.metacloudservice.serverside.bukkit.events.InteractEvent;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.TimerTask;
import java.util.UUID;

import static org.json.XMLTokener.entity;

public class SignBootstrap extends JavaPlugin {


    public static SignDriver signDriver;
    public static SignsAPI signsAPI;

    public static  SignBootstrap instance;
    public static  Integer empty;
    public static  Integer online;
    public static  Integer full;
    public static  Integer maintenance;
    public static  Integer searching;

    @Override
    public void onEnable() {
        instance = this;
        signsAPI = new SignsAPI();
        signDriver = new SignDriver();
        PluginDriver.getInstance().register(new Command());
        Bukkit.getPluginManager().registerEvents(new InteractEvent(), this);
        CloudAPI.getInstance().getEventDriver().registerListener(new CloudEventHandler());
        signsAPI.getSigns().forEach(location -> {
            Location loc = new Location(Bukkit.getWorld(location.getLocationWorld()), location.getLocationPosX(), location.getLocationPosY(), location.getLocationPosZ());
            signDriver.addSign(UUID.fromString(location.getSignUUID()), location.getGroupName(), loc);
        });
        empty = 0;
        online = 0;
        full = 0;
        maintenance = 0;
        searching = 0;
        SignUpdaterTask updaterTask = new SignUpdaterTask(SignBootstrap.signsAPI);
        updaterTask.runTaskTimer(SignBootstrap.instance, 0L, 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null) != null &&signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null).isUseKnockBack()){
                signDriver.getAllSigns().forEach(cloudSign -> {
                    for (Entity entity : cloudSign.getLocation().getWorld().getNearbyEntities(cloudSign.getLocation(), signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null).getKnockbackDistance(),signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null).getKnockbackDistance(),
                            signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null).getKnockbackDistance())
                    ){
                        if (entity instanceof Player && !entity.hasPermission("metacloud.bypass.signs.knockback")) {
                            Location entityLocation = entity.getLocation();
                            if (cloudSign.getLocation().getBlock().getState() instanceof org.bukkit.block.Sign) {
                                entity.setVelocity(new Vector(entityLocation.getX() - cloudSign.getLocation().getX(),
                                        entityLocation.getY() - cloudSign.getLocation().getY(),
                                        entityLocation.getZ() - cloudSign.getLocation().getZ()).normalize()
                                        .multiply(signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null).getKnockbackStrength())
                                        .setY(0.2D));
                            }
                        }
                        }

                });
            }
        }, 20, 20);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


}
