package eu.metacloudservice.cloudplayer.intefaces;

public interface ICloudPlayerRestCech {


    void handleConnect(String proxyService);
    void handleDisconnect();
    void handleSwitch(String nextService);


}
