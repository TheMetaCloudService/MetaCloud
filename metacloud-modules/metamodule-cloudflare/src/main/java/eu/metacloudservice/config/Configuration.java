/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.config;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter@Setter
public class Configuration implements IConfigAdapter {

    private String email;
    private String domain;
    private String apiToken;
    private String zoneID;

    private ArrayList<FlareGroup> groups;

    public Configuration(String email, String domain, String apiToken, String zoneID, ArrayList<FlareGroup> groups) {
        this.email = email;
        this.domain = domain;
        this.apiToken = apiToken;
        this.zoneID = zoneID;
        this.groups = groups;
    }

    public Configuration() {
    }
}
