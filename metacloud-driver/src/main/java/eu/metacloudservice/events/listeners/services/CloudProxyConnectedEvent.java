package eu.metacloudservice.events.listeners.services;

import eu.metacloudservice.events.entrys.IEventAdapter;


public class CloudProxyConnectedEvent extends IEventAdapter {
    @lombok.Getter
    private final String name;

    @lombok.Getter
    private final String node;

    @lombok.Getter
    private final Integer port;

    @lombok.Getter
    private final String host;

    @lombok.Getter
    private final String group;

    public CloudProxyConnectedEvent(String name, String node, Integer port, String host, String group) {
        this.name = name;
        this.node = node;
        this.port = port;
        this.host = host;
        this.group = group;
    }
}
