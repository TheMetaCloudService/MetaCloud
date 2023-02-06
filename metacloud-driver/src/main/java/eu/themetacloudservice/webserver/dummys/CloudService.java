package eu.themetacloudservice.webserver.dummys;

import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;

public class CloudService implements IConfigAdapter {

    private String name;
    private Integer players;
    private String status;


    public CloudService() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlayers() {
        return players;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPlayers(Integer players) {
        this.players = players;
    }

    public String getStatus() {
        return status;
    }
}
