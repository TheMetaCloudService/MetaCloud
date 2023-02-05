package eu.themetacloudservice.events.dummys.processbased;

import eu.themetacloudservice.events.entry.IEventAdapter;

public class ServiceDisconnectedEvent extends IEventAdapter {

    private String name;

    public ServiceDisconnectedEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
