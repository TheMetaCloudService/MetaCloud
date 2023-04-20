package eu.metacloudservice.events.listeners.player;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudPlayerDisconnectedEvent extends IEventAdapter {
    private final String name;

    private final String UniqueId;

    public String getName() {
        return this.name;
    }

    public String getUniqueId() {
        return this.UniqueId;
    }

    public CloudPlayerDisconnectedEvent(String name, String uniqueId) {
        this.name = name;
        this.UniqueId = uniqueId;
    }
}
