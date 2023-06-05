package eu.metacloudservice.configuration.dummys.restapi;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import java.util.HashMap;

public class PacketConfig implements IConfigAdapter {

    @lombok.Setter
    @lombok.Getter
    private String logo;
    @lombok.Setter
    @lombok.Getter
    private String plugin;
    @lombok.Setter
    @lombok.Getter
    private String api;
    @lombok.Setter
    @lombok.Getter
    private HashMap<String, String> spigots;
    @lombok.Setter
    @lombok.Getter
    private HashMap<String, String> bungeecords;
    @lombok.Setter
    @lombok.Getter
    private HashMap<String, String> modules;


    public PacketConfig(){

    }

}
