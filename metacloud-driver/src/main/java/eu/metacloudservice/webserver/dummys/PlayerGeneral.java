package eu.metacloudservice.webserver.dummys;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class PlayerGeneral implements IConfigAdapter {

    private ArrayList<String> cloudplayers;

    public ArrayList<String> getCloudplayers() {
        return cloudplayers;
    }

    public void setCloudplayers(ArrayList<String> cloudplayers) {
        this.cloudplayers = cloudplayers;
    }
}
