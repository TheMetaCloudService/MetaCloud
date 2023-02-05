package eu.themetacloudservice.events.dummys.processbased;

import eu.themetacloudservice.events.entry.IEventAdapter;

public class ServiceLaunchEvent extends IEventAdapter {

    private final String name;
    private final String node;

    public ServiceLaunchEvent(String name, String node) {
        this.name = name;
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public String getNode() {
        return node;
    }
}
