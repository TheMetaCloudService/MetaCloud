package eu.metacloudservice.configuration.dummys.message;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

public class Messages implements IConfigAdapter {


    private String prefix;
    private String successfullyConnected;
    private String serviceIsFull;
    private String alreadyOnFallback;
    private String connectingGroupMaintenance;
    private String noFallbackServer;
    private String kickNetworkIsFull;
    private String kickNetworkIsMaintenance;
    private String kickNoFallback;
    private String kickOnlyProxyJoin;
    private String kickAlreadyOnNetwork;


    public Messages(String prefix, String successfullyConnected, String serviceIsFull, String alreadyOnFallback, String connectingGroupMaintenance, String noFallbackServer, String kickNetworkIsFull, String kickNetworkIsMaintenance, String kickNoFallback, String kickOnlyProxyJoin, String kickAlreadyOnNetwork) {
        this.prefix = prefix;
        this.successfullyConnected = successfullyConnected;
        this.serviceIsFull = serviceIsFull;
        this.alreadyOnFallback = alreadyOnFallback;
        this.connectingGroupMaintenance = connectingGroupMaintenance;
        this.noFallbackServer = noFallbackServer;
        this.kickNetworkIsFull = kickNetworkIsFull;
        this.kickNetworkIsMaintenance = kickNetworkIsMaintenance;
        this.kickNoFallback = kickNoFallback;
        this.kickOnlyProxyJoin = kickOnlyProxyJoin;
        this.kickAlreadyOnNetwork = kickAlreadyOnNetwork;
    }

    public Messages() {
    }

    public String getKickAlreadyOnNetwork() {
        return kickAlreadyOnNetwork;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuccessfullyConnected() {
        return successfullyConnected;
    }

    public String getServiceIsFull() {
        return serviceIsFull;
    }

    public String getAlreadyOnFallback() {
        return alreadyOnFallback;
    }

    public String getConnectingGroupMaintenance() {
        return connectingGroupMaintenance;
    }

    public String getNoFallbackServer() {
        return noFallbackServer;
    }

    public String getKickNetworkIsFull() {
        return kickNetworkIsFull;
    }

    public String getKickNetworkIsMaintenance() {
        return kickNetworkIsMaintenance;
    }

    public String getKickNoFallback() {
        return kickNoFallback;
    }

    public String getKickOnlyProxyJoin() {
        return kickOnlyProxyJoin;
    }
}
