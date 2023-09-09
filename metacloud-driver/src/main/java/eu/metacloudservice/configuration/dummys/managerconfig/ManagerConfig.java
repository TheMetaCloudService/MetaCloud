package eu.metacloudservice.configuration.dummys.managerconfig;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class ManagerConfig implements IConfigAdapter {

    @lombok.Setter
    @lombok.Getter
    private String managerAddress;
    @lombok.Setter
    @lombok.Getter
    private String language;
    @lombok.Setter
    @lombok.Getter
    private String splitter;
    @lombok.Setter
    @lombok.Getter
    private String uuid;
    @lombok.Setter
    @lombok.Getter
    private boolean useProtocol;
    @lombok.Setter
    @lombok.Getter
    private boolean autoUpdate;
    @lombok.Setter
    @lombok.Getter
    private Integer processorUsage;
    @lombok.Setter
    @lombok.Getter
    private Integer serviceStartupCount;
    @lombok.Setter
    @lombok.Getter
    private boolean showConnectingPlayers;
    @lombok.Setter
    @lombok.Getter
    private Integer canUsedMemory;
    @lombok.Setter
    @lombok.Getter
    private Integer bungeecordPort;
    @lombok.Setter
    @lombok.Getter
    private Integer spigotPort;
    @lombok.Setter
    @lombok.Getter
    private String bungeecordVersion;
    @lombok.Setter
    @lombok.Getter
    private String spigotVersion;
    @lombok.Setter
    @lombok.Getter
    private Integer networkingCommunication;
    @lombok.Setter
    @lombok.Getter
    private Integer restApiCommunication;
    @lombok.Setter
    @lombok.Getter
    private Integer timeOutCheckTime;
    @lombok.Setter
    @lombok.Getter
    private boolean copyLogs;

    @lombok.Setter
    @lombok.Getter
    private ArrayList<String> whitelist;
    @lombok.Setter
    @lombok.Getter
    private ArrayList<ManagerConfigNodes> nodes;

    public ManagerConfig(){
    }

    public boolean getUseProtocol() {
        return useProtocol;
    }

}
