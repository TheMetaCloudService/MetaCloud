package eu.themetacloudservice.configuration.dummys.managerconfig;

import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class ManagerConfig implements IConfigAdapter {

    private String language;
    private String splitter;
    private String managerAddress;
    private Integer canUsedMemory;
    private String bungeecordVersion;
    private String spigotVersion;
    private Integer networkingCommunication;
    private Integer restApiCommunication;
    private ArrayList<ManagerConfigNodes> nodes;

    public ManagerConfig(){

    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSplitter() {
        return splitter;
    }

    public void setSplitter(String splitter) {
        this.splitter = splitter;
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

    public ArrayList<ManagerConfigNodes> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<ManagerConfigNodes> nodes) {
        this.nodes = nodes;
    }
}
