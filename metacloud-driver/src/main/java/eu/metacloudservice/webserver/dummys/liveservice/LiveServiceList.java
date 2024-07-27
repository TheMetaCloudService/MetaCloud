package eu.metacloudservice.webserver.dummys.liveservice;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class LiveServiceList implements IConfigAdapter {


    private String cloudServiceSplitter;
    private ArrayDeque<String> cloudServices;

    public LiveServiceList() {}

    public String getCloudServiceSplitter() {
        return cloudServiceSplitter;
    }

    public ArrayDeque<String> getCloudServices() {
        return cloudServices;
    }

    public void  remove(@NonNull final String service){
        cloudServices.removeIf(s -> s.equalsIgnoreCase(service));
    }

    public void setCloudServiceSplitter(@NonNull final String cloudServiceSplitter) {
        this.cloudServiceSplitter = cloudServiceSplitter;
    }

    public void setCloudServices(@NonNull final ArrayDeque<String> cloudServices) {
        this.cloudServices = cloudServices;
    }
}
