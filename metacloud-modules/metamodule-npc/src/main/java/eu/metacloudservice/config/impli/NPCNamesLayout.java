/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.config;

public class NPCNamesLayout {

    public String defaultLine1;
    public String defaultLine2;

    public String maintenanceLine1;
    public String maintenanceLine2;

    public NPCNamesLayout(String defaultLine1, String defaultLine2, String maintenanceLine1, String maintenanceLine2) {
        this.defaultLine1 = defaultLine1;
        this.defaultLine2 = defaultLine2;
        this.maintenanceLine1 = maintenanceLine1;
        this.maintenanceLine2 = maintenanceLine2;
    }

    public String getDefaultLine1() {
        return defaultLine1;
    }

    public String getDefaultLine2() {
        return defaultLine2;
    }

    public String getMaintenanceLine1() {
        return maintenanceLine1;
    }

    public String getMaintenanceLine2() {
        return maintenanceLine2;
    }
}
