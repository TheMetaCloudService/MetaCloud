package eu.metacloudservice.events.listeners.player;


import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudPlayerConnectedEvent extends IEventAdapter {
    @lombok.Getter
    private final String name;

    @lombok.Getter
    private final String proxy;

    @lombok.Getter
    private final String UniqueId;

    public CloudPlayerConnectedEvent(String name, String proxy, String UniqueId) {
        this.name = name;
        this.proxy = proxy;
        this.UniqueId = UniqueId;
    }
}
