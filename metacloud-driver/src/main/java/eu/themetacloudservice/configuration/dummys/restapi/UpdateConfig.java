package eu.themetacloudservice.configuration.dummys.restapi;

import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;

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
