package eu.metacloudservice.configuration.dummys.serviceconfig;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;


public class LiveService implements IConfigAdapter {

    @lombok.Setter
    @lombok.Getter
    private String service;
    @lombok.Setter
    @lombok.Getter
    private String group;
    @lombok.Setter
    @lombok.Getter
    private String managerAddress;
    @lombok.Setter
    @lombok.Getter
    private String runningNode;
    @lombok.Setter
    @lombok.Getter
    private Integer port;
    @lombok.Setter
    @lombok.Getter
    private Integer restPort;
    @lombok.Setter
    @lombok.Getter
    private Integer networkPort;

    public LiveService() {}

}
