package eu.metacloudservice.events.listeners.player;


import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudPlayerConnectedEvent extends IEventAdapter {
    private final String name;

    private final String proxy;

    private final String UniqueId;

    public String getName() {
        return this.name;
    }

    public String getProxy() {
        return this.proxy;
    }

    public String getUniqueId() {
        return this.UniqueId;
    }

    public CloudPlayerConnectedEvent(String name, String proxy, String UniqueId) {
        this.name = name;
        this.proxy = proxy;
        this.UniqueId = UniqueId;
    }
}
