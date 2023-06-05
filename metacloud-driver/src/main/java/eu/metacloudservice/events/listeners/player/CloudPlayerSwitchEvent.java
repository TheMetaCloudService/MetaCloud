package eu.metacloudservice.events.listeners.player;


import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudPlayerSwitchEvent extends IEventAdapter {
    @lombok.Getter
    private final String name;

    @lombok.Getter
    private final String UniqueId;

    @lombok.Getter
    private final String from;
    @lombok.Getter
    private final String to;

    public CloudPlayerSwitchEvent(String name, String uniqueId, String from, String to) {
        this.name = name;
        this.UniqueId = uniqueId;
        this.from = from;
        this.to = to;
    }
}
