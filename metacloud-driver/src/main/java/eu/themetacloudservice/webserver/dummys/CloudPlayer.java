package eu.themetacloudservice.webserver.dummys;

import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;

public class CloudPlayer implements IConfigAdapter {


    private String uuid, username;
    private String currentProxy, currentServer;


    public CloudPlayer() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentProxy() {
        return currentProxy;
    }

    public void setCurrentProxy(String currentProxy) {
        this.currentProxy = currentProxy;
    }

    public String getCurrentServer() {
        return currentServer;
    }

    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }
}
