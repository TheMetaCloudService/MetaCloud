package eu.themetacloudservice.bungeecord.listeners;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.bungeecord.CloudPlugin;
import eu.themetacloudservice.bungeecord.utils.LobbyEntry;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.message.Messages;
import eu.themetacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.themetacloudservice.events.dummys.cloudplayerbased.CloudPlayerSwitchServiceEvent;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerChangeService;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerConnect;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerDisconnect;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.webserver.dummys.WhitelistConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;

public class CloudConnectListener implements Listener {


    private boolean bypassMaintenance;
    private boolean bypassFullNetwork;
    private ArrayList<ProxiedPlayer> connected;

    public CloudConnectListener() {
        connected = new ArrayList<>();
    }

    @EventHandler
    public void  handle(final PostLoginEvent event){
        WhitelistConfig whitelistConfig = (WhitelistConfig)(new ConfigDriver()).convert(CloudPlugin.getInstance().getRestDriver().get("/whitelist"), WhitelistConfig.class);


        this.bypassMaintenance = event.getPlayer().hasPermission("metacloud.connection.maintenance");

       this.bypassFullNetwork = event.getPlayer().hasPermission("metacloud.connection.full");


       if (whitelistConfig.getWhitelist().contains(event.getPlayer().getName())){
           this.bypassFullNetwork = true;
           this.bypassMaintenance = true;
       }
        LiveService service = (LiveService)(new ConfigDriver("./CLOUDSERVICE.json")).read(LiveService.class);
        PackageCloudPlayerConnect connect = new PackageCloudPlayerConnect(event.getPlayer().getUUID(), event.getPlayer().getName(), service.getService());

        NettyDriver.getInstance().nettyClient.sendPacket(connect);

        Group group = (Group)(new ConfigDriver()).convert(CloudPlugin.getInstance().getRestDriver().get("/" + service.getGroup()), Group.class);

        if (group.isMaintenance() && !this.bypassMaintenance) {
            Messages messages = (Messages)(new ConfigDriver()).convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
            event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNetworkIsMaintenance()).replace("&", "§"));

            return;
        }
        if (CloudPlugin.getInstance().getCurrentPlayers() >= group.getMaxPlayers() && !this.bypassFullNetwork) {
            Messages messages = (Messages)(new ConfigDriver()).convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
            event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNetworkIsFull()).replace("&", "§"));


        }
       if (CloudPlugin.getInstance().getLobbyDriver().findMatchingLobby(event.getPlayer()) == null){
           Messages messages = (Messages)(new ConfigDriver()).convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
           event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNoFallback()).replace("&", "§"));

       }



    }



    @EventHandler
    public void handle(ServerConnectEvent event) {
        if (event.isCancelled()) return;

        if (event.getPlayer().getServer() == null){
            LobbyEntry target = CloudPlugin.getInstance().getLobbyDriver().findMatchingLobby(event.getPlayer());
            connected.add(event.getPlayer());
            if (target == null){
                Messages messages = (Messages)(new ConfigDriver()).convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNoFallback()).replace("&", "§"));
            }

            event.setTarget(CloudPlugin.getInstance().getServerDriver().getServerInfo(target.getName()));
        }else {
            if (event.getTarget().getName().equalsIgnoreCase("lobby")){
                LobbyEntry target = CloudPlugin.getInstance().getLobbyDriver().findMatchingLobby(event.getPlayer());

                if (target == null){
                    Messages messages = (Messages)(new ConfigDriver()).convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                    event.getPlayer().disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNoFallback()).replace("&", "§"));
                }

                event.setTarget(CloudPlugin.getInstance().getServerDriver().getServerInfo(target.getName()));
            }
        }


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(final PlayerDisconnectEvent event) {
        connected.remove(event.getPlayer());

        PackageCloudPlayerDisconnect disconnect = new PackageCloudPlayerDisconnect(event.getPlayer().getUUID(), event.getPlayer().getName());
        NettyDriver.getInstance().nettyClient.sendPacket(disconnect);

    }

    @EventHandler(priority = EventPriority.LOW)
    public void handle(final ServerSwitchEvent event){

        if (connected.contains(event.getPlayer())){
            if (event.getFrom() == null){
                PackageCloudPlayerChangeService service = new PackageCloudPlayerChangeService(event.getPlayer().getName(), event.getPlayer().getServer().getInfo().getName(), "");
                NettyDriver.getInstance().nettyClient.sendPacket(service);
            }else {
                PackageCloudPlayerChangeService service = new PackageCloudPlayerChangeService(event.getPlayer().getName(), event.getPlayer().getServer().getInfo().getName(), event.getFrom().getName());
                NettyDriver.getInstance().nettyClient.sendPacket(service);
            }


        }

    }


    @EventHandler
    public void handle(final ServerKickEvent event) {
        LobbyEntry lobby = CloudPlugin.getInstance().getLobbyDriver().findMatchingLobby(event.getPlayer(), event.getKickedFrom().getName());

        if (lobby != null){
            event.setCancelServer(ProxyServer.getInstance().getServerInfo(lobby.getName()));
            event.setCancelled(true);
        }

    }



 }
