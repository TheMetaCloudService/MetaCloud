package eu.metacloudservice.bootstrap.bungee.listener;


import eu.metacloudservice.bootstrap.bungee.utils.ServerDriver;
import eu.metacloudservice.events.entrys.EventHandler;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.listeners.CloudServiceConnectedEvent;
import eu.metacloudservice.events.listeners.CloudServiceDisconnectedEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

public class CloudEvents implements ICloudListener {


    @EventHandler(priority = 100)
    public void handle(CloudServiceConnectedEvent event){
        ServerInfo info = ProxyServer.getInstance().constructServerInfo(event.getName(), new InetSocketAddress(event.getHost(), event.getPort()), "metacloud-service", false);
        ProxyServer.getInstance().getConsole().sendMessage("[§bMetaCloud§r] > added ~ " +event.getName());
        new ServerDriver().addServer(info);
    }

    @EventHandler
    public void handle(CloudServiceDisconnectedEvent event){
        ProxyServer.getInstance().getConsole().sendMessage("[§bMetaCloud§r] > remove ~ " +event.getName());
        new ServerDriver().removeServer(event.getName());
    }

}
