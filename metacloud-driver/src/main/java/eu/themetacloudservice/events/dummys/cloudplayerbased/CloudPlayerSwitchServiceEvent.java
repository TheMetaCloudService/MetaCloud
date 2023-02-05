package eu.themetacloudservice.events.dummys.cloudplayerbased;

import eu.themetacloudservice.events.entry.IEventAdapter;

public class CloudPlayerSwitchServiceEvent  extends IEventAdapter {

    private String name;
    private String lastServer;
    private String newServer;

    public CloudPlayerSwitchServiceEvent(String name, String lastServer, String newServer) {
        this.name = name;
        this.lastServer = lastServer;
        this.newServer = newServer;
    }

    public String getName() {
        return name;
    }

    public String getLastServer() {
        return lastServer;
    }

    public String getNewServer() {
        return newServer;
    }
}
