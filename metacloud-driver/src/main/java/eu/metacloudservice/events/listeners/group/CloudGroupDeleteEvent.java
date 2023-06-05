package eu.metacloudservice.events.listeners.group;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudGroupDeleteEvent extends IEventAdapter {

    @lombok.Getter
    private final String groupname;

    public CloudGroupDeleteEvent(String groupname) {
        this.groupname = groupname;
    }

}
