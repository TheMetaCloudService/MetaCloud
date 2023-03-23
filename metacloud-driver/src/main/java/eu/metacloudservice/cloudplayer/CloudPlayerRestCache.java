package eu.metacloudservice.cloudplayer;

import eu.metacloudservice.cloudplayer.intefaces.ICloudPlayerRestCache;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import eu.metacloudservice.webserver.interfaces.IRest;

public class CloudPlayerRestCache implements ICloudPlayerRestCache, IConfigAdapter, IRest {

    private  String  cloudplayername;
    private  String  cloudplayeruuid;
    private  String cloudplayerproxy;
    private  String cloudplayerservice;
    private Long cloudplayerconnect;




    public CloudPlayerRestCache(String name, String uuid) {
        this.cloudplayername = name;
        this.cloudplayeruuid = uuid;
        cloudplayerconnect = System.currentTimeMillis();
    }
    public CloudPlayerRestCache() {}

    @Override
    public void handleConnect(String cloudplayerproxy) {
        this.cloudplayerproxy = cloudplayerproxy;
    }

    @Override
    public void handleDisconnect() {

    }

    @Override
    public void handleSwitch(String cloudplayerservice) {
        this.cloudplayerservice = cloudplayerservice;
    }

    public String getCloudplayername() {
        return cloudplayername;
    }


    public String getCloudplayeruuid() {
        return cloudplayeruuid;
    }

    public String getCloudplayerproxy() {
        return cloudplayerproxy;
    }

    public void setCloudplayerproxy(String cloudplayerproxy) {
        this.cloudplayerproxy = cloudplayerproxy;
    }

    public String getCloudplayerservice() {
        return cloudplayerservice;
    }

    public void setCloudplayerservice(String cloudplayerservice) {
        this.cloudplayerservice = cloudplayerservice;
    }

    public Long getCloudplayerconnect() {
        return cloudplayerconnect;
    }

}
