package eu.metacloudservice.api;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.Locations;
import eu.metacloudservice.config.SignLocation;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SignsAPI {

    public Configuration configuration;
     public Locations Locations;

    public SignsAPI() {

        if (!new File("./service.json").exists()){
            configuration = ((Configuration) new ConfigDriver().convert( CloudAPI.getInstance().getRestDriver().get("/module/signs/configuration"), Configuration.class));
            Locations = ((Locations) new ConfigDriver().convert( CloudAPI.getInstance().getRestDriver().get("/module/signs/locations"), Locations.class));
        }
    }

    public ArrayList<SignLocation> getSigns(){
       return getLocConfig().getLocations();
    }

    public ArrayList<String> getUUIDs(){
        ArrayList<String> uuids = new ArrayList<>();
        getLocConfig().getLocations().forEach(signLocation -> {
            uuids.add(signLocation.getSignUUID());
        });
        return uuids;
    }

    public boolean canFind(Location location){
        return getLocations().stream().anyMatch(location1 -> location1.getX() == location.getX() && location1.getY() == location.getY() && location1.getZ() == location.getZ() && location1.getWorld().getName().equalsIgnoreCase(location.getWorld().getName()));
    }

    public ArrayList<Location> getLocations(){
        ArrayList<Location> uuids = new ArrayList<>();
        getLocConfig().getLocations().forEach(signLocation -> {
            uuids.add(new Location(Bukkit.getWorld(signLocation.getLocationWorld()), signLocation.getLocationPosX(),signLocation.getLocationPosY(), signLocation.getLocationPosZ()));
        });
        return uuids;
    }

    public Locations getLocConfig(){
        return this.Locations;
    }
    public Configuration getConfig(){
        return this.configuration;
    }

    public void createSign(SignLocation location){
        if (getSigns().stream().noneMatch(location1 -> location1.getSignUUID().equals(location.getSignUUID()))){

            Locations l =  Locations;
            l.getLocations().add(location);
            Locations = l;
            CloudAPI.getInstance().getRestDriver().update("/module/signs/locations", new ConfigDriver().convert(l));
        }
    }

    public void removeSign(String  signUUID){
        if (getSigns().stream().anyMatch(location1 -> location1.getSignUUID().equals(signUUID))){
            ArrayList<SignLocation> update = getSigns();
            Locations l =  Locations;
            l.getLocations().removeIf(location -> location.getSignUUID().equals(signUUID));
            Locations = l;
            CloudAPI.getInstance().getRestDriver().update("/module/signs/locations", new ConfigDriver().convert(l));
        }
    }
}
