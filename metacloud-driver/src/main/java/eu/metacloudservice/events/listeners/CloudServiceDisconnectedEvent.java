package eu.metacloudservice.events.listeners;


import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudServiceDisconnectedEvent extends IEventAdapter {
    private final String name;

    public String getName() {
        return this.name;
    }

    public CloudServiceDisconnectedEvent(String name) {
        this.name = name;
    }
}
