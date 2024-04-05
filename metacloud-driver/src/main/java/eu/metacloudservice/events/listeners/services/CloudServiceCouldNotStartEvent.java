package eu.metacloudservice.events.listeners.services;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudServiceCouldNotStartEvent extends IEventAdapter {

    @lombok.Getter
    private final String name;

    public CloudServiceCouldNotStartEvent(String name) {
        this.name = name;
    }
}
