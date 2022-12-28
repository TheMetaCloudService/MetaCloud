package eu.themetacloudservice.configuration.dummys.nodeconfig;

import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;

public class NodeConfig implements IConfigAdapter {

    private String language;
    private String managerAddress;
    private Integer canUsedMemory;
    private String bungeecordVersion;
    private String spigotVersion;
    private Integer networkingCommunication;
    private Integer restApiCommunication;
    private String nodeName;
    private String nodeAddress;


    public NodeConfig(){

    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getManagerAddress() {
        return managerAddress;
    }

    public void setManagerAddress(String managerAddress) {
        this.managerAddress = managerAddress;
    }

    public Integer getCanUsedMemory() {
        return canUsedMemory;
    }

    public void setCanUsedMemory(Integer canUsedMemory) {
        this.canUsedMemory = canUsedMemory;
    }

    public String getBungeecordVersion() {
        return bungeecordVersion;
    }

    public void setBungeecordVersion(String bungeecordVersion) {
        this.bungeecordVersion = bungeecordVersion;
    }

    public String getSpigotVersion() {
        return spigotVersion;
    }

    public void setSpigotVersion(String spigotVersion) {
        this.spigotVersion = spigotVersion;
    }

    public Integer getNetworkingCommunication() {
        return networkingCommunication;
    }

    public void setNetworkingCommunication(Integer networkingCommunication) {
        this.networkingCommunication = networkingCommunication;
    }

    public Integer getRestApiCommunication() {
        return restApiCommunication;
    }

    public void setRestApiCommunication(Integer restApiCommunication) {
        this.restApiCommunication = restApiCommunication;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }
}
