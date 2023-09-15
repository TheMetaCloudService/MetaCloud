package eu.metacloudservice.api;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.Locations;
import eu.metacloudservice.config.SignLocation;
import eu.metacloudservice.configuration.ConfigDriver;

import java.util.ArrayList;

public class SignsAPI {


    public SignsAPI() {}

    public ArrayList<SignLocation> getSigns(){
       return ((Locations) new ConfigDriver().convert( CloudAPI.getInstance().getRestDriver().get("/module/signs/locations"), Locations.class)).getLocations();
    }

    public String getUUIDFormLocation(int x, int y, int z, String world){
        ArrayList<SignLocation> locations = getSigns();
        SignLocation finalLoc =  locations.stream().filter(location -> location.getLocationPosX() == x && location.getLocationPosY() == y && location.getLocationPosZ() == z && location.getLocationWorld().equals(world)).findFirst().orElse(null);
        if (finalLoc == null) return null;
        else {
            return finalLoc.getSignUUID();
        }
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
