package eu.themetacloudservice.events.dummys.cloudplayerbased;

import eu.themetacloudservice.events.entry.ICloudEventListener;
import eu.themetacloudservice.events.entry.IEventAdapter;

public class CloudPlayerConnectEvent extends IEventAdapter {

    private String name;
    private String proxy;
     private String uuid;

    public CloudPlayerConnectEvent(String name, String proxy, String uuid) {
        this.name = name;
        this.proxy = proxy;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getProxy() {
        return proxy;
    }

    public String getUuid() {
        return uuid;
    }
}
