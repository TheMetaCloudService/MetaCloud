package eu.metacloudservice.webserver.dummys;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class WhiteList implements IConfigAdapter {


    private ArrayList<String> whitelist;

    public WhiteList() {
    }

    public ArrayList<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(ArrayList<String> whitelist) {
        this.whitelist = whitelist;
    }
}
