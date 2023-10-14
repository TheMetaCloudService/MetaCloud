/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.config;

import eu.metacloudservice.config.enums.ClickAction;
import eu.metacloudservice.config.impli.ItemLayout;
import eu.metacloudservice.config.impli.NPCConfig;
import eu.metacloudservice.config.impli.NPCNamesLayout;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class Configuration implements IConfigAdapter {


    public ArrayList<NPCConfig> configurations;

    public Configuration() {
    }

    public Configuration(ArrayList<NPCConfig> configuration) {
        this.configurations = configuration;
    }

    public ArrayList<NPCConfig> getConfigurations() {
        return configurations;
    }
}
