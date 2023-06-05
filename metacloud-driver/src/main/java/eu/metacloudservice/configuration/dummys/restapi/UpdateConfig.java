package eu.metacloudservice.configuration.dummys.restapi;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

public class UpdateConfig implements IConfigAdapter {

    @lombok.Setter
    @lombok.Getter
    private String latestReleasedVersion;

    public UpdateConfig(){

    }

}
