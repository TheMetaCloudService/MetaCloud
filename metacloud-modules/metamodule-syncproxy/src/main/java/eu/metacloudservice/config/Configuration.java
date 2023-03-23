package eu.metacloudservice.config;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class Configuration implements IConfigAdapter {

    private ArrayList<DesignConfig> configuration;

    public ArrayList<DesignConfig> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ArrayList<DesignConfig> configuration) {
        this.configuration = configuration;
    }
}
