package eu.themetacloudservice.network.service.proxyconnect;

public class Service {
    private String name;
    private Integer port;
    private String host;
    private String groupConfig;


    public Service() {
    }

    public Service(String name, Integer port, String host, String groupConfig) {
        this.name = name;
        this.port = port;
        this.host = host;
        this.groupConfig = groupConfig;
    }

    public String getGroupConfig() {
        return groupConfig;
    }

    public String getName() {
        return name;
    }

    public Integer getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}
