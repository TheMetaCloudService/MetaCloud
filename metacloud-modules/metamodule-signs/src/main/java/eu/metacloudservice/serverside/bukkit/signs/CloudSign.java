package eu.metacloudservice.serverside.bukkit.signs;

import org.bukkit.Location;

public class CloudSign {

    private Location location;
    private String signID;

    public CloudSign(Location location, String signID) {
        this.location = location;
        this.signID = signID;
    }
}
