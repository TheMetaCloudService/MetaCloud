package eu.themetacloudservice.events.dummys.processbased;

import eu.themetacloudservice.events.entry.IEventAdapter;

public class ServiceJoinQueueEvent extends IEventAdapter {

    private final String name;
    private final String node;


    public ServiceJoinQueueEvent(String name, String node) {
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
