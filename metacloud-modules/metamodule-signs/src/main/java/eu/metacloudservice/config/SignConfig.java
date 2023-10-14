/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.config;

import java.util.ArrayList;

public class SignConfig {

    String targetGroup;

    boolean hideFull;
    boolean useKnockBack;
    double knockbackDistance;
    double knockbackStrength;

    ArrayList<SignLayout> empty;
    ArrayList<SignLayout> online;
    ArrayList<SignLayout> full;
    ArrayList<SignLayout> maintenance;
    ArrayList<SignLayout> searching;

    public SignConfig() {
    }


    public SignConfig(String targetGroup, boolean hideFull, boolean useKnockBack, double knockbackDistance, double knockbackStrength, ArrayList<SignLayout> empty, ArrayList<SignLayout> online, ArrayList<SignLayout> full, ArrayList<SignLayout> maintenance, ArrayList<SignLayout> searching) {
        this.targetGroup = targetGroup;
        this.hideFull = hideFull;
        this.useKnockBack = useKnockBack;
        this.knockbackDistance = knockbackDistance;
        this.knockbackStrength = knockbackStrength;
        this.empty = empty;
        this.online = online;
        this.full = full;
        this.maintenance = maintenance;
        this.searching = searching;
    }

    public boolean isHideFull() {
        return hideFull;
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

    public ArrayList<SignLayout> getEmpty() {
        return empty;
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

    public String getTargetGroup() {
        return targetGroup;
    }
}
