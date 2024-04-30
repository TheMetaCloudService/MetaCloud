package eu.metacloudservice.config.migration;

import eu.metacloudservice.config.Tablist;

import java.util.ArrayList;

public class MigrationDesignConfig {


    private String targetGroup;
    private boolean motdEnabled;
    private boolean tabEnabled;
    private ArrayList<MigrationMotd> maintenancen;
    private ArrayList<MigrationMotd> defaults;
    private ArrayList<Tablist> tablist;

    public MigrationDesignConfig() {
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

    public ArrayList<MigrationMotd> getMaintenancen() {
        return maintenancen;
    }
    public void setMaintenancen(ArrayList<MigrationMotd> maintenancen) {
        this.maintenancen = maintenancen;
    }

    public ArrayList<MigrationMotd> getDefaults() {
        return defaults;
    }

    public void setDefaults(ArrayList<MigrationMotd> defaults) {
        this.defaults = defaults;
    }

    public ArrayList<Tablist> getTablist() {
        return tablist;
    }

    public void setTablist(ArrayList<Tablist> tablist) {
        this.tablist = tablist;
    }
}
