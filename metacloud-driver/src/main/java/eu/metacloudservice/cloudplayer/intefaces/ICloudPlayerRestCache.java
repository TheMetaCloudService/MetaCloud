package eu.metacloudservice.cloudplayer.intefaces;

public interface ICloudPlayerRestCache {


    void handleConnect(String proxyService);
    void handleDisconnect();
    void handleSwitch(String nextService);


}
