package eu.metacloudservice.serverside.bukkit;

import eu.metacloudservice.api.SignsAPI;
import eu.metacloudservice.serverside.bukkit.commands.SignCommand;
import eu.metacloudservice.serverside.bukkit.listener.SignListener;
import eu.metacloudservice.serverside.bukkit.signs.CloudSign;
import eu.metacloudservice.serverside.bukkit.signs.SignDriver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class BukkitBootstrap extends JavaPlugin {

    private static BukkitBootstrap instance;
    private SignDriver signDriver;

    public SignsAPI signsAPI;


    @Override
    public void onEnable() {
        instance = this;
        signDriver = new SignDriver();
        signsAPI = new SignsAPI();
        Bukkit.getPluginManager().registerEvents(new SignListener(), this);
        getCommand("cloudsign").setExecutor(new SignCommand());
        signsAPI.getSigns().forEach(location -> {
            Location loc = new Location(Bukkit.getWorld(location.getLocationWorld()), location.getLocationPosX(), location.getLocationPosY(), location.getLocationPosZ());
            signDriver.handleCreateSign(new CloudSign(UUID.fromString(location.getSignUUID()), loc, location.getGroupName()));
        });


    }
    public static BukkitBootstrap getInstance() {
        return instance;
    }

    public SignDriver getSignDriver() {
        return signDriver;
    }

}
