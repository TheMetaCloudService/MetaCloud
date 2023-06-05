package eu.metacloudservice.events.listeners.services;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudProxyLaunchEvent extends IEventAdapter {

    @lombok.Getter
    private final String name;

    @lombok.Getter
    private final String group;

    @lombok.Getter
    private final String node;

    public CloudProxyLaunchEvent(String name, String group, String node) {
        this.name = name;
        this.group = group;
        this.node = node;
    }
}
