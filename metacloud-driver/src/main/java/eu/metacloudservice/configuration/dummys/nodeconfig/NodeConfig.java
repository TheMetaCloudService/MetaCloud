package eu.metacloudservice.configuration.dummys.nodeconfig;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

public class NodeConfig implements IConfigAdapter {

    private String language;
    private String managerAddress;
    private Integer processorUsage;
    private boolean useViaVersion;
    private Integer canUsedMemory;
    private String bungeecordVersion;
    private Integer bungeecordPort;
    private String spigotVersion;
    private Integer spigotPort;
    private Integer networkingCommunication;
    private Integer restApiCommunication;
    private String nodeName;
    private String nodeAddress;

    private boolean copyLogs;


    public NodeConfig(){

    }

    public boolean isCopyLogs() {
        return copyLogs;
    }

    public void setCopyLogs(boolean copyLogs) {
        this.copyLogs = copyLogs;
    }

    public Integer getProcessorUsage() {
        return processorUsage;
    }

    public void setProcessorUsage(Integer processorUsage) {
        this.processorUsage = processorUsage;
    }

    public boolean isUseViaVersion() {
        return useViaVersion;
    }

    public void setUseViaVersion(boolean useViaVersion) {
        this.useViaVersion = useViaVersion;
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
