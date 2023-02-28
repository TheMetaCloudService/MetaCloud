package eu.metacloudservice;

import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.client.NettyClient;

public class CloudAPI {

    private static CloudAPI instance;
    private final LiveService service;

    public CloudAPI() {
        instance = this;

        new Driver();
        new NettyDriver();
        service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        NettyDriver.getInstance().nettyClient = new NettyClient();
        NettyDriver.getInstance().nettyClient.bind(service.getManagerAddress(), service.getNetworkPort()).connect();

    }

    public static CloudAPI getInstance() {
        return instance;
    }
}
