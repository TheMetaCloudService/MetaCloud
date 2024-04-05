package eu.metacloudservice.config.migration;

import eu.metacloudservice.config.DesignConfig;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class MigrationConfiguration implements IConfigAdapter {

    private ArrayList<MigrationDesignConfig> configuration;

    public MigrationConfiguration() {}

    public ArrayList<MigrationDesignConfig> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ArrayList<MigrationDesignConfig> configuration) {
        this.configuration = configuration;
    }
}
