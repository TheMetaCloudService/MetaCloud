package eu.metacloudservice.events.listeners.restapi;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudRestAPIDeleteEvent extends IEventAdapter {
    @lombok.Getter
    private final String path;

    public CloudRestAPIDeleteEvent(String path) {
        this.path = path;
    }
}
