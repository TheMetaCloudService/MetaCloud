package eu.metacloudservice.config.player;

import eu.metacloudservice.config.player.entry.GivenGroup;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerConfiguration implements IConfigAdapter {

    private String UUID;

    private ArrayList<GivenGroup> groups;
    private HashMap<String, Boolean> permissions;


    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public ArrayList<GivenGroup> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<GivenGroup> groups) {
        this.groups = groups;
    }

    public HashMap<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(HashMap<String, Boolean> permissions) {
        this.permissions = permissions;
    }
}
