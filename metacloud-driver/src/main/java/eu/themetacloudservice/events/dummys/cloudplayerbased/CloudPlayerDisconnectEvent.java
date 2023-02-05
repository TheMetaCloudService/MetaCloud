package eu.themetacloudservice.events.dummys.cloudplayerbased;

import eu.themetacloudservice.events.entry.IEventAdapter;

public class CloudPlayerDisconnectEvent extends IEventAdapter {

    private String name;
    private String uuid;

    public CloudPlayerDisconnectEvent(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }
}
