package eu.metacloudservice.api;

import eu.metacloudservice.Driver;
import eu.metacloudservice.config.Locations;
import eu.metacloudservice.config.SignLocation;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.serverside.bukkit.BukkitBootstrap;
import eu.metacloudservice.webserver.RestDriver;

import java.util.ArrayList;

public class SignsAPI {


    public SignsAPI() {}

    private ArrayList<SignLocation> getSigns(){
       return ((Locations) new ConfigDriver().convert( BukkitBootstrap.getInstance().getRestDriver().get("/module/signs/locations"), Locations.class)).getLocations();
    }

    public String getUUIDFormLocation(int x, int y, int z, String world){
        ArrayList<SignLocation> locations = getSigns();
        SignLocation finalLoc =  locations.stream().filter(location -> location.getLocationPosX() == x && location.getLocationPosY() == y && location.getLocationPosZ() == z && location.getLocationWorld().equals(world)).findFirst().orElse(null);
        if (finalLoc == null) return null;
        else {
            return finalLoc.getSignUUID();
        }
    }
    
    public void createSign(SignLocation location){
        if (getSigns().stream().noneMatch(location1 -> location1.getSignUUID().equals(location.getSignUUID()))){
            ArrayList<SignLocation> update = getSigns();
            Locations l =  (Locations) new ConfigDriver().convert(new RestDriver().get("/module/signs/locations"), Locations.class);
            l.getLocations().add(location);
            BukkitBootstrap.getInstance().getRestDriver().put("/module/signs/locations", new ConfigDriver().convert(l));
        }
    }

    public void removeSign(String  signUUID){
        if (getSigns().stream().anyMatch(location1 -> location1.getSignUUID().equals(signUUID))){
            ArrayList<SignLocation> update = getSigns();
            Locations l =  (Locations) new ConfigDriver().convert(new RestDriver().get("/module/signs/locations"), Locations.class);
            l.getLocations().removeIf(location -> location.getSignUUID().equals(signUUID));
            BukkitBootstrap.getInstance().getRestDriver().put("/module/signs/locations", new ConfigDriver().convert(l));
        }
    }
}
