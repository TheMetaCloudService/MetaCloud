/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.config;

import eu.metacloudservice.config.impli.NPCLocation;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class Locations implements IConfigAdapter {

    ArrayList<NPCLocation> locations;

    public Locations() {
    }

    public Locations(ArrayList<NPCLocation> locations) {
        this.locations = locations;
    }

    public ArrayList<NPCLocation> getLocations() {
        return locations;
    }
}
