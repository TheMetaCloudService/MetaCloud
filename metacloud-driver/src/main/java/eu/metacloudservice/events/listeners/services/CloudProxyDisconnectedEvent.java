package eu.metacloudservice.events.listeners.services;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudProxyDisconnectedEvent extends IEventAdapter {
    @lombok.Getter
    private final String name;

    public CloudProxyDisconnectedEvent(String name) {
        this.name = name;
    }
}
