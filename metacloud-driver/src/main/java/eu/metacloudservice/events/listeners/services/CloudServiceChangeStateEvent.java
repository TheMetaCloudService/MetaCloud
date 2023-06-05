package eu.metacloudservice.events.listeners.services;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudServiceChangeStateEvent extends IEventAdapter {

    @lombok.Getter
    private final String name;
    @lombok.Getter
    private final String state;

    public CloudServiceChangeStateEvent(String name, String state) {
        this.name = name;
        this.state = state;
    }

}
