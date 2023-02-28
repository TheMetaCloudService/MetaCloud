package eu.metacloudservice.configuration.dummys.serviceconfig;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;


public class LiveService implements IConfigAdapter {

    private String service;
    private String group;
    private String managerAddress;
    private String runningNode;
    private Integer port;
    private Integer restPort;
    private Integer networkPort;

    public LiveService() {}

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getManagerAddress() {
        return managerAddress;
    }

    public void setManagerAddress(String managerAddress) {
        this.managerAddress = managerAddress;
    }

    public String getRunningNode() {
        return runningNode;
    }

    public void setRunningNode(String runningNode) {
        this.runningNode = runningNode;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getRestPort() {
        return restPort;
    }

    public void setRestPort(Integer restPort) {
        this.restPort = restPort;
    }

    public Integer getNetworkPort() {
        return networkPort;
    }

    public void setNetworkPort(Integer networkPort) {
        this.networkPort = networkPort;
    }
}
