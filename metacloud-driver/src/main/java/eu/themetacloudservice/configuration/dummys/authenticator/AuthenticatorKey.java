package eu.themetacloudservice.configuration.dummys.authenticator;

import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;

public class AuthenticatorKey implements IConfigAdapter {

    private String key;

    public AuthenticatorKey() {}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
