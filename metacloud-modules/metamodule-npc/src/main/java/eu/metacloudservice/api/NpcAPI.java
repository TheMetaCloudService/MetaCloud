/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.api;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.Locations;
import eu.metacloudservice.config.impli.NPCConfig;
import eu.metacloudservice.config.impli.NPCLocation;
import eu.metacloudservice.configuration.ConfigDriver;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.ArrayList;

public class NpcAPI {

    public Configuration configuration;
    public Locations Locations;

    public NpcAPI() {
        if (!new File("./service.json").exists()){
            configuration = ((Configuration) new ConfigDriver().convert( CloudAPI.getInstance().getRestDriver().get("/module/npc/configuration"), Configuration.class));
            Locations = ((Locations) new ConfigDriver().convert( CloudAPI.getInstance().getRestDriver().get("/module/npc/locations"), Locations.class));
        }
    }

    public ArrayList<NPCLocation> getSigns(){
        return getLocConfig().getLocations();
    }

    public boolean canFind(Location location){
        return getLocations().stream().anyMatch(location1 -> location1.getX() == location.getX() && location1.getY() == location.getY() && location1.getZ() == location.getZ() && location1.getWorld().getName().equalsIgnoreCase(location.getWorld().getName()));
    }


    public NPCConfig getNPCConfig(String group){
        return configuration.getConfigurations().stream().filter(config -> config.targetGroup.equalsIgnoreCase(group)).findFirst().orElse(null);
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

    public void createNPC(NPCLocation location){
        if (getSigns().stream().noneMatch(location1 -> location1.getNpcUUID().equalsIgnoreCase(location.getNpcUUID()))){
            Locations l =  Locations;
            l.getLocations().add(location);
            Locations = l;
            CloudAPI.getInstance().getRestDriver().update("/module/npc/locations", new ConfigDriver().convert(l));
        }
    }

    public void removeNPC(String  signUUID){
        if (getSigns().stream().anyMatch(location1 -> location1.getNpcUUID().equals(signUUID))){
            ArrayList<NPCLocation> update = getSigns();
            Locations l =  Locations;
            l.getLocations().removeIf(location -> location.getNpcUUID().equals(signUUID));
            Locations = l;
            CloudAPI.getInstance().getRestDriver().update("/module/npc/locations", new ConfigDriver().convert(l));
        }
    }
}
