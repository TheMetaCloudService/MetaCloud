package eu.metacloudservice.webserver.dummys;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class PlayerGeneral implements IConfigAdapter {

    private ArrayList<String> cloudplayers;

    public ArrayList<String> getCloudplayers() {
        return cloudplayers;
    }

    public void setCloudplayers(@NonNull final ArrayList<String> cloudplayers) {
        this.cloudplayers = cloudplayers;
    }
}
