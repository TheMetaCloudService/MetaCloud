package eu.metacloudservice.events.listeners.group;

import eu.metacloudservice.events.entrys.IEventAdapter;

public class CloudGroupDeleteEvent extends IEventAdapter {

    private final String groupname;

    public CloudGroupDeleteEvent(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupname() {
        return groupname;
    }
}
