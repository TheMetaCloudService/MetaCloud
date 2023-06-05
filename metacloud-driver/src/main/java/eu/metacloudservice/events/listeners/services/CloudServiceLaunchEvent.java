package eu.metacloudservice.events.listeners.services;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudServiceLaunchEvent extends IEventAdapter {

    @lombok.Getter
    private final String name;

    @lombok.Getter
    private final String group;

    @lombok.Getter
    private final String node;

    public CloudServiceLaunchEvent(String name, String group, String node) {
        this.name = name;
        this.group = group;
        this.node = node;
    }
}
