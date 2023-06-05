package eu.metacloudservice.events.listeners.restapi;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudRestAPIPutEvent extends IEventAdapter {

    @lombok.Getter
    private final String path;
    @lombok.Getter
    private final String content;

    public CloudRestAPIPutEvent(String path, String content) {
        this.path = path;
        this.content = content;
    }

}
