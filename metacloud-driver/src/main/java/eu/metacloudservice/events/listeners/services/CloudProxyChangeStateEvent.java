package eu.metacloudservice.events.listeners.services;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudProxyChangeStateEvent extends IEventAdapter {

    @lombok.Getter
    private final String name;
    @lombok.Getter
    private final String state;

    public CloudProxyChangeStateEvent(String name, String state) {
        this.name = name;
        this.state = state;
    }

}
