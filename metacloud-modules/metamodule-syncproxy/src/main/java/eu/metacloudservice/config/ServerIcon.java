package eu.metacloudservice.config;

import lombok.Getter;

public class ServerIcon {
    @Getter
    private String maintenanceIcon;
    @Getter
    private String defaultIcon;

    public ServerIcon() {
    }

    public ServerIcon(String maintenanceIcon, String defaultIcon) {
        this.maintenanceIcon = maintenanceIcon;
        this.defaultIcon = defaultIcon;
    }
}
