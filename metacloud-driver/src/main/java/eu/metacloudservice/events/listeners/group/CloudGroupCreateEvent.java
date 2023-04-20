package eu.metacloudservice.events.listeners.group;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudGroupCreateEvent extends IEventAdapter {

    private final String groupname;

    public CloudGroupCreateEvent(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupname() {
        return groupname;
    }
}
