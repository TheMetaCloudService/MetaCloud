package eu.metacloudservice.webserver.dummys.liveservice;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class LiveServiceList implements IConfigAdapter {

    private ArrayList<String> services;
    private String splitter;

    public LiveServiceList() {}

    public ArrayList<String> getServices() {
        return services;
    }


    public void setServices(ArrayList<String> services) {
        this.services = services;
    }

    public void setSplitter(String splitter) {
        this.splitter = splitter;
    }

    public String getSplitter() {
        return splitter;
    }
}
