package eu.metacloudservice.configuration.dummys.restapi;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

public class UpdateConfig implements IConfigAdapter {

    private String latestReleasedVersion;

    public UpdateConfig(){

    }

    public String getLatestReleasedVersion() {
        return latestReleasedVersion;
    }

    public void setLatestReleasedVersion(String latestReleasedVersion) {
        this.latestReleasedVersion = latestReleasedVersion;
    }
}
