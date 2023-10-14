package eu.metacloudservice.config;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class Configuration implements IConfigAdapter {


    public  ArrayList<SignConfig> configurations;
    public Configuration() {
    }

    public Configuration(ArrayList<SignConfig> configurations) {
        this.configurations = configurations;
    }
}
