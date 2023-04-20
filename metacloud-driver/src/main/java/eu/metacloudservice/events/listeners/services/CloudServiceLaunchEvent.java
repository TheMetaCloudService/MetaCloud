package eu.metacloudservice.events.listeners;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudServiceLaunchEvent extends IEventAdapter {

    private final String name;

    private final String group;

    private final String node;

    public String getName() {
        return this.name;
    }

    public String getGroup() {
        return this.group;
    }

    public String getNode() {
        return this.node;
    }

    public CloudServiceLaunchEvent(String name, String group, String node) {
        this.name = name;
        this.group = group;
        this.node = node;
    }
}
