package eu.metacloudservice.events.listeners.player;

import eu.metacloudservice.events.entrys.IEventAdapter;

import java.util.UUID;

public class CloudPlayerDisconnectedEvent extends IEventAdapter {
    @lombok.Getter
    private final String name;

    @lombok.Getter
    private final UUID UniqueId;

    public CloudPlayerDisconnectedEvent(String name, UUID uniqueId) {
        this.name = name;
        this.UniqueId = uniqueId;
    }
}
