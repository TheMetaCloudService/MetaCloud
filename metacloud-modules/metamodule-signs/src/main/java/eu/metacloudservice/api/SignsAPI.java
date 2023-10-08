package eu.metacloudservice.api;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.Locations;
import eu.metacloudservice.config.SignLocation;
import eu.metacloudservice.configuration.ConfigDriver;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

public class SignsAPI {


    public SignsAPI() {}

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
        return ((Locations) new ConfigDriver().convert( CloudAPI.getInstance().getRestDriver().get("/module/signs/locations"), Locations.class));
    }
    public Configuration getConfig(){
        return ((Configuration) new ConfigDriver().convert( CloudAPI.getInstance().getRestDriver().get("/module/signs/configuration"), Configuration.class));
    }

    public void createSign(SignLocation location){            System.out.println("TEST#1");
        if (getSigns().stream().noneMatch(location1 -> location1.getSignUUID().equals(location.getSignUUID()))){

            Locations l =  (Locations) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/module/signs/locations"), Locations.class);
            l.getLocations().add(location);
            CloudAPI.getInstance().getRestDriver().put("/module/signs/locations", new ConfigDriver().convert(l));
        }
    }

    public void removeSign(String  signUUID){
        if (getSigns().stream().anyMatch(location1 -> location1.getSignUUID().equals(signUUID))){
            ArrayList<SignLocation> update = getSigns();
            Locations l =  (Locations) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/module/signs/locations"), Locations.class);
            l.getLocations().removeIf(location -> location.getSignUUID().equals(signUUID));
            CloudAPI.getInstance().getRestDriver().put("/module/signs/locations", new ConfigDriver().convert(l));
        }
    }
}
