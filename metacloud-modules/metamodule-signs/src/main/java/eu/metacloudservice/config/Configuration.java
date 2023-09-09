package eu.metacloudservice.config;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class Configuration implements IConfigAdapter {


    boolean hideFull;
    boolean useKnockBack;
    double knockbackDistance;
    double knockbackStrength;

    ArrayList<SignLayout> empty;
    ArrayList<SignLayout> online;
    ArrayList<SignLayout> full;
    ArrayList<SignLayout> maintenance;
    ArrayList<SignLayout> searching;

    public Configuration() {
    }

    public Configuration(boolean hideFull,ArrayList<SignLayout> empty, ArrayList<SignLayout> online, ArrayList<SignLayout> full, ArrayList<SignLayout> maintenance, ArrayList<SignLayout> searching, boolean useKnockBack, double knockbackDistance, double knockbackStrength) {
        this.hideFull = hideFull;
        this.empty = empty;
        this.online = online;
        this.full = full;
        this.maintenance = maintenance;
        this.searching = searching;
        this.useKnockBack = useKnockBack;
        this.knockbackStrength = knockbackStrength;
        this.knockbackDistance = knockbackDistance;

    }

    public ArrayList<SignLayout> getEmpty() {
        return empty;
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

    public boolean isUseKnockBack() {
        return useKnockBack;
    }

    public double getKnockbackDistance() {
        return knockbackDistance;
    }

    public double getKnockbackStrength() {
        return knockbackStrength;
    }
}
