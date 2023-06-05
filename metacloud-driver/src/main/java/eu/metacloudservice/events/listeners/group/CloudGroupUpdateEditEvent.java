package eu.metacloudservice.events.listeners.group;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudGroupUpdateEditEvent extends IEventAdapter {


    @lombok.Getter
    private final String groupname;


    public CloudGroupUpdateEditEvent(String groupname) {
        this.groupname = groupname;
    }
}
