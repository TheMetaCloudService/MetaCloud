package eu.metacloudservice.configuration.dummys.message;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

public class Messages implements IConfigAdapter {


    @lombok.Getter
    private String prefix;
    @lombok.Getter
    private String successfullyConnected;
    @lombok.Getter
    private String serviceIsFull;
    @lombok.Getter
    private String alreadyOnFallback;
    @lombok.Getter
    private String connectingGroupMaintenance;
    @lombok.Getter
    private String noFallbackServer;
    @lombok.Getter
    private String kickNetworkIsFull;
    @lombok.Getter
    private String kickNetworkIsMaintenance;
    @lombok.Getter
    private String kickNoFallback;
    @lombok.Getter
    private String kickOnlyProxyJoin;
    @lombok.Getter
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

}
