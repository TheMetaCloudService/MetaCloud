package eu.metacloudservice.events.listeners.restapi;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudRestAPIUpdateEvent extends IEventAdapter {

    @lombok.Getter
    private final String path;
    @lombok.Getter
    private final String content;

    public CloudRestAPIUpdateEvent(String path, String content) {
        this.path = path;
        this.content = content;
    }

}
