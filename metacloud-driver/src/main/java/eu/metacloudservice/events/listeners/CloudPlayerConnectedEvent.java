package eu.metacloudservice.events.dummys.playerbased;

import eu.metacloudservice.events.entry.IEventAdapter;

public class CloudPlayerConnectedEvent {
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
