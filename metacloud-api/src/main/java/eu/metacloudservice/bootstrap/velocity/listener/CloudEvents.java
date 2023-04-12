package eu.metacloudservice.bootstrap.velocity.listener;


import eu.metacloudservice.bootstrap.velocity.VelocityBootstrap;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Priority;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.CloudServiceConnectedEvent;
import eu.metacloudservice.events.listeners.CloudServiceDisconnectedEvent;

import java.net.InetSocketAddress;

public class CloudEvents implements ICloudListener {


    @Subscribe(priority = Priority.HIGHEST)
    public void handle(CloudServiceConnectedEvent event){
        VelocityBootstrap.proxyServer.registerServer(new com.velocitypowered.api.proxy.server.ServerInfo(event.getName(), new InetSocketAddress(event.getHost(), event.getPort())));
    }

    @Subscribe
    public void handle(CloudServiceDisconnectedEvent event){
        VelocityBootstrap.proxyServer.unregisterServer(VelocityBootstrap.proxyServer.getServer(event.getName()).get().getServerInfo());
    }

}
