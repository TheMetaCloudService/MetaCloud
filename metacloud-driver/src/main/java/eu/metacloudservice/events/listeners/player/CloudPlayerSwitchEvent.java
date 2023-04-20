package eu.metacloudservice.events.listeners.player;


import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudPlayerSwitchEvent extends IEventAdapter {
    private final String name;

    private final String UniqueId;

    private final String from;
    private final String to;

    public String getName() {
        return this.name;
    }

    public String getUniqueId() {
        return this.UniqueId;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public CloudPlayerSwitchEvent(String name, String uniqueId, String from, String to) {
        this.name = name;
        this.UniqueId = uniqueId;
        this.from = from;
        this.to = to;
    }
}
