package eu.metacloudservice.events.listeners.player;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudPlayerDisconnectedEvent extends IEventAdapter {
    @lombok.Getter
    private final String name;

    @lombok.Getter
    private final String UniqueId;

    public CloudPlayerDisconnectedEvent(String name, String uniqueId) {
        this.name = name;
        this.UniqueId = uniqueId;
    }
}
