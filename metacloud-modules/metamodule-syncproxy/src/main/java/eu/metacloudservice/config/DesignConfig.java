package eu.metacloudservice.config;

import java.util.ArrayList;

public class DesignConfig {


    private String targetGroup;
    private boolean motdEnabled;
    private boolean tabEnabled;
    private ArrayList<Motd> maintenancen;
    private ArrayList<Motd> defaults;
    private ArrayList<Tablist> tablist;

    public DesignConfig() {
    }



    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }

    public boolean isMotdEnabled() {
        return motdEnabled;
    }

    public void setMotdEnabled(boolean motdEnabled) {
        this.motdEnabled = motdEnabled;
    }

    public boolean isTabEnabled() {
        return tabEnabled;
    }

    public void setTabEnabled(boolean tabEnabled) {
        this.tabEnabled = tabEnabled;
    }

    public ArrayList<Motd> getMaintenancen() {
        return maintenancen;
    }
    public void setMaintenancen(ArrayList<Motd> maintenancen) {
        this.maintenancen = maintenancen;
    }

    public ArrayList<Motd> getDefaults() {
        return defaults;
    }

    public void setDefaults(ArrayList<Motd> defaults) {
        this.defaults = defaults;
    }

    public ArrayList<Tablist> getTablist() {
        return tablist;
    }

    public void setTablist(ArrayList<Tablist> tablist) {
        this.tablist = tablist;
    }
}
