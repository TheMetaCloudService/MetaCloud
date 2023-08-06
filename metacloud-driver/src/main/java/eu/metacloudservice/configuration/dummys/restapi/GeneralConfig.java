package eu.metacloudservice.configuration.dummys.restapi;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.HashMap;

public class GeneralConfig implements IConfigAdapter {


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
    private String latestReleasedVersion;


    public GeneralConfig(){

    }

}
