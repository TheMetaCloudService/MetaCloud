package eu.metacloudservice.configuration.dummys.restapi;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.HashMap;

public class ModuleConfig implements IConfigAdapter {

    @lombok.Setter
    @lombok.Getter
    private HashMap<String, String> modules;

    public ModuleConfig() {
    }
}
