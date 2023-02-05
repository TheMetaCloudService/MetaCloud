package eu.themetacloudservice.events.dummys.processbased;

import eu.themetacloudservice.events.entry.IEventAdapter;

public class ServiceConnectedEvent extends IEventAdapter {

    private final String service;
    private final String node;
    private final Integer port;
    private final String  host;
    private final String group;

    public ServiceConnectedEvent(String service, String node, Integer port, String host, String group) {
        this.service = service;
        this.node = node;
        this.port = port;
        this.host = host;
        this.group = group;
    }

    public String getService() {
        return service;
    }

    public String getGroup() {
        return group;
    }

    public String getNode() {
        return node;
    }

    public Integer getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}
