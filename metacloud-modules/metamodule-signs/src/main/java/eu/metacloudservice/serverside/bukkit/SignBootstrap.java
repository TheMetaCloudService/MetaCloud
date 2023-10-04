/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.SignsAPI;
import eu.metacloudservice.serverside.bukkit.commands.SignCommand;
import eu.metacloudservice.serverside.bukkit.drivers.SignDriver;
import eu.metacloudservice.serverside.bukkit.entry.CloudSign;
import eu.metacloudservice.serverside.bukkit.events.CloudEvents;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class SignBootstrap extends JavaPlugin {


    public static SignDriver signDriver;
    public static SignsAPI signsAPI;

    @Override
    public void onEnable() {
        signsAPI = new SignsAPI();
        signDriver = new SignDriver();
        getCommand("cloudsign").setExecutor(new SignCommand());
        CloudAPI.getInstance().registerListener(new CloudEvents());
        signsAPI.getSigns().forEach(location -> {
            Location loc = new Location(Bukkit.getWorld(location.getLocationWorld()), location.getLocationPosX(), location.getLocationPosY(), location.getLocationPosZ());
            signDriver.registerSign(new CloudSign(UUID.fromString(location.getSignUUID()), location.getGroupName(), loc));
        });

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
