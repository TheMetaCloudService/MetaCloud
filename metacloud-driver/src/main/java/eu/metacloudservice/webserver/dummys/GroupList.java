package eu.metacloudservice.webserver.dummys;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class GroupList implements IConfigAdapter {

    private ArrayList<String> groups;

    public GroupList() {
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }
}
