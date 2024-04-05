package eu.metacloudservice.events.listeners.services;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudProxyCouldNotStartEvent extends IEventAdapter {

    @lombok.Getter
    private final String name;

    public CloudProxyCouldNotStartEvent(String name) {
        this.name = name;
    }
}
