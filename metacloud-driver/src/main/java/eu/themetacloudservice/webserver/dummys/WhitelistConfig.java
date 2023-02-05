package eu.themetacloudservice.webserver.dummys;

import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class WhitelistConfig implements IConfigAdapter {


    private ArrayList<String> whitelist;

    public WhitelistConfig() {
    }

    public ArrayList<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(ArrayList<String> whitelist) {
        this.whitelist = whitelist;
    }
}
