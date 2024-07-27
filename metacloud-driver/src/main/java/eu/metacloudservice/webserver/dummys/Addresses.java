package eu.metacloudservice.webserver.dummys;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.NonNull;

import java.util.ArrayList;

public class Addresses implements IConfigAdapter {

    private ArrayList<String> whitelist;

    public Addresses() {
    }

    public ArrayList<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(@NonNull final ArrayList<String> whitelist) {
        this.whitelist = whitelist;
    }
}
