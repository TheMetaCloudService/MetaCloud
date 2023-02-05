package eu.themetacloudservice.configuration.dummys.managerconfig;

import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class ManagerConfig implements IConfigAdapter {

    private String managerAddress;
    private String language;
    private String splitter;
    private String uuid;
    private boolean useProtocol;
    private Integer canUsedMemory;
    private Integer bungeecordPort;
    private Integer spigotPort;
    private String bungeecordVersion;
    private String spigotVersion;
    private Integer networkingCommunication;
    private Integer restApiCommunication;

    private ArrayList<String> whitelist;
    private ArrayList<ManagerConfigNodes> nodes;

    public ManagerConfig(){
    }


    public ArrayList<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(ArrayList<String> whitelist) {
        this.whitelist = whitelist;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean getUseProtocol() {
        return useProtocol;
    }

    public void setUseProtocol(boolean userProtocol) {
        this.useProtocol = userProtocol;
    }

    public Integer getBungeecordPort() {
        return bungeecordPort;
    }

    public void setBungeecordPort(Integer bungeecordPort) {
        this.bungeecordPort = bungeecordPort;
    }

    public Integer getSpigotPort() {
        return spigotPort;
    }

    public void setSpigotPort(Integer spigotPort) {
        this.spigotPort = spigotPort;
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
