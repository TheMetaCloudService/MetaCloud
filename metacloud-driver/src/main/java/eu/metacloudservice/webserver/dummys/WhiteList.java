package eu.metacloudservice.webserver.dummys;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class WhiteList implements IConfigAdapter {


    private ArrayList<String> whitelist;

    public WhiteList() {
    }

    public ArrayList<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(@NonNull final ArrayList<String> whitelist) {
        this.whitelist = whitelist;
    }
}
