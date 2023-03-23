package eu.metacloudservice.config.player;

import eu.metacloudservice.config.player.entry.GivenGroup;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class PlayerConfiguration implements IConfigAdapter {

    private String UUID;

    private ArrayList<GivenGroup> groups;
    private ArrayList<String> permissions;


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

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }
}
