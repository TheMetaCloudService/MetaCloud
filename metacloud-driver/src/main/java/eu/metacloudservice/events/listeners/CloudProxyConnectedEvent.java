package eu.metacloudservice.events.listeners;

import eu.metacloudservice.events.entrys.IEventAdapter;


public class CloudProxyConnectedEvent extends IEventAdapter {
    private final String name;

    private final String node;

    private final Integer port;

    private final String host;

    private final String group;

    public String getName() {
        return this.name;
    }

    public String getNode() {
        return this.node;
    }

    public Integer getPort() {
        return this.port;
    }

    public String getHost() {
        return this.host;
    }

    public String getGroup() {
        return this.group;
    }

    public CloudProxyConnectedEvent(String name, String node, Integer port, String host, String group) {
        this.name = name;
        this.node = node;
        this.port = port;
        this.host = host;
        this.group = group;
    }
}
