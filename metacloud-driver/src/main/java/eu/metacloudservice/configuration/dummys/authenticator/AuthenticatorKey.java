package eu.metacloudservice.configuration.dummys.authenticator;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

public class AuthenticatorKey implements IConfigAdapter {

    @lombok.Setter
    @lombok.Getter
    private String key;
    public AuthenticatorKey() {}

}
