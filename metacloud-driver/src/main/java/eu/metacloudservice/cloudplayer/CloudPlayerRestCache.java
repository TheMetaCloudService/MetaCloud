package eu.metacloudservice.cloudplayer;

import eu.metacloudservice.cloudplayer.intefaces.ICloudPlayerRestCech;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import eu.metacloudservice.webserver.interfaces.IRest;

public class CloudPlayerRestCache implements ICloudPlayerRestCech, IConfigAdapter, IRest {

    private  String  name;
    private  String  uuid;
    private  String currentProxy;
    private  String currentService;

    public CloudPlayerRestCache(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public CloudPlayerRestCache() {}

    @Override
    public void handleConnect(String proxyService) {
        this.currentProxy = proxyService;
    }

    @Override
    public void handleDisconnect() {

    }

    @Override
    public void handleSwitch(String nextService) {
        this.currentService = nextService;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getCurrentService() {
        return currentService;
    }

    public void setCurrentService(String currentService) {
        this.currentService = currentService;
    }

    public String getCurrentProxy() {
        return currentProxy;
    }
}
