package eu.metacloudservice.webserver.dummys.liveservice;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import eu.metacloudservice.process.ServiceState;

public class LiveServices implements IConfigAdapter {


    private String name;
    private int uuid;
    private String group;
    private int players;
    private String host;
    private String node;
    private int port;
    private ServiceState state;

    public LiveServices() {
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }


    public ServiceState getState() {
        return state;
    }

    public void setState(ServiceState state) {
        this.state = state;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
