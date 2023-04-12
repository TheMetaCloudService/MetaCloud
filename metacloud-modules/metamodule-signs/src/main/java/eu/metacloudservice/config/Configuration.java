package eu.metacloudservice.config;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class Configuration implements IConfigAdapter {


    boolean hideFull;

    ArrayList<SignLayout> online;
    ArrayList<SignLayout> full;
    ArrayList<SignLayout> maintenance;
    ArrayList<SignLayout> searching;

    public Configuration() {
    }

    public Configuration(boolean hideFull, ArrayList<SignLayout> online, ArrayList<SignLayout> full, ArrayList<SignLayout> maintenance, ArrayList<SignLayout> searching) {
        this.hideFull = hideFull;
        this.online = online;
        this.full = full;
        this.maintenance = maintenance;
        this.searching = searching;
    }

    public boolean isHideFull() {
        return hideFull;
    }

    public ArrayList<SignLayout> getOnline() {
        return online;
    }

    public ArrayList<SignLayout> getFull() {
        return full;
    }

    public ArrayList<SignLayout> getMaintenance() {
        return maintenance;
    }

    public ArrayList<SignLayout> getSearching() {
        return searching;
    }
}
