package eu.metacloudservice.configuration.dummys.nodeconfig;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

public class NodeConfig implements IConfigAdapter {

    @lombok.Setter
    @lombok.Getter
    private String language;
    @lombok.Setter
    @lombok.Getter
    private String managerAddress;
    @lombok.Setter
    @lombok.Getter
    private Integer processorUsage;
    @lombok.Setter
    @lombok.Getter
    private boolean autoUpdate;
    @lombok.Setter
    @lombok.Getter
    private Integer canUsedMemory;
    @lombok.Setter
    @lombok.Getter
    private String bungeecordVersion;
    @lombok.Setter
    @lombok.Getter
    private Integer bungeecordPort;
    @lombok.Setter
    @lombok.Getter
    private String spigotVersion;
    @lombok.Setter
    @lombok.Getter
    private Integer spigotPort;
    @lombok.Setter
    @lombok.Getter
    private Integer networkingCommunication;
    @lombok.Setter
    @lombok.Getter
    private Integer restApiCommunication;
    @lombok.Setter
    @lombok.Getter
    private String nodeName;
    @lombok.Setter
    @lombok.Getter
    private String nodeAddress;
    @lombok.Setter
    @lombok.Getter
    private boolean copyLogs;

    public NodeConfig(){

    }

}
