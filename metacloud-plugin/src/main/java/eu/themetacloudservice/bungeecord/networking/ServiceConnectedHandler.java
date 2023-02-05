package eu.themetacloudservice.bungeecord.networking;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.bungeecord.CloudPlugin;
import eu.themetacloudservice.bungeecord.utils.LobbyEntry;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.events.dummys.cloudplayerbased.CloudPlayerConnectEvent;
import eu.themetacloudservice.events.dummys.cloudplayerbased.CloudPlayerDisconnectEvent;
import eu.themetacloudservice.events.dummys.cloudplayerbased.CloudPlayerSwitchServiceEvent;
import eu.themetacloudservice.events.dummys.processbased.ServiceConnectedEvent;
import eu.themetacloudservice.events.dummys.processbased.ServiceDisconnectedEvent;
import eu.themetacloudservice.events.dummys.processbased.ServiceJoinQueueEvent;
import eu.themetacloudservice.events.dummys.processbased.ServiceLaunchEvent;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerChangeService;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerConnect;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerDisconnect;
import eu.themetacloudservice.network.service.*;
import eu.themetacloudservice.network.service.proxyconnect.PackageConnectedProxyCallBack;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.webserver.dummys.CloudPlayer;
import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

import java.net.InetSocketAddress;

public class ServiceConnectedHandler implements IPacketListener {
    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {
        if (paramPacket instanceof PackageConnectedServiceToALL){
            PackageConnectedServiceToALL packet = (PackageConnectedServiceToALL) paramPacket;

            Group group = (Group) new ConfigDriver().convert(packet.getGroupConfig(), Group.class);
            if (group.getGroupType().equalsIgnoreCase("LOBBY")){
                CloudPlugin.getInstance().getLobbyDriver().getLobby().add(new LobbyEntry(packet.getName(), group.getPermission(), group.getPriority(), group.getGroup()));
            }

            if (!group.getGroupType().equalsIgnoreCase("PROXY")){
                CloudPlugin.getInstance().getServerDriver().addServer(ProxyServer.getInstance().constructServerInfo(packet.getName(), new InetSocketAddress(packet.getAddress(), packet.getPort()),
                        "metacloud-cloudservice",
                        false));
            }
        }if(paramPacket instanceof PackageShutdownServiceToALL){
            PackageShutdownServiceToALL packet = (PackageShutdownServiceToALL) paramPacket;
            if (CloudPlugin.getInstance().getLobbyDriver().getLobby().stream().anyMatch(lobbyEntry -> lobbyEntry.getName().equalsIgnoreCase(packet.getName()))){
                CloudPlugin.getInstance().getLobbyDriver().getLobby().removeIf(lobbyEntry -> lobbyEntry.getName().equalsIgnoreCase(packet.getName()))
;            }

            if (CloudPlugin.getInstance().getServerDriver().serverExists(packet.getName())){
                CloudPlugin.getInstance().getServerDriver().removeServer(packet.getName());
            }
        }


        //EVENTS
        if (paramPacket instanceof PackageConnectedServiceToALL) {
            PackageConnectedServiceToALL packet = (PackageConnectedServiceToALL) paramPacket;
            Driver.getInstance().getEventDriver().executeEvent(new ServiceConnectedEvent(packet.getName(), packet.getNode(), packet.getPort(), packet.getAddress(), packet.getGroup()));
        }
        if (paramPacket instanceof PackageLaunchServiceToALL){
            Driver.getInstance().getEventDriver().executeEvent(new ServiceLaunchEvent(((PackageLaunchServiceToALL) paramPacket).getName(), ((PackageLaunchServiceToALL) paramPacket).getNode()));
        }  if (paramPacket instanceof PackageShutdownServiceToALL){
            Driver.getInstance().getEventDriver().executeEvent(new ServiceDisconnectedEvent(((PackageShutdownServiceToALL) paramPacket).getName()));
        } if (paramPacket instanceof PackageRegisterServiceToALL){
            Driver.getInstance().getEventDriver().executeEvent(new ServiceJoinQueueEvent(((PackageRegisterServiceToALL) paramPacket).getName(), ((PackageRegisterServiceToALL) paramPacket).getNode()));
        }

        if (paramPacket instanceof PackageCloudPlayerChangeService){
            CloudPlayer cloudPlayer = (CloudPlayer) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("cloudplayer~" + ((PackageCloudPlayerChangeService) paramPacket).getName()), CloudPlayer.class);
            Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerSwitchServiceEvent(((PackageCloudPlayerChangeService) paramPacket).getName(), cloudPlayer.getCurrentServer(), ((PackageCloudPlayerChangeService) paramPacket).getServer()));

        }
        if (paramPacket instanceof PackageCloudPlayerDisconnect){
            Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerDisconnectEvent(((PackageCloudPlayerDisconnect) paramPacket).getName(), ((PackageCloudPlayerDisconnect) paramPacket).getUuid()));
        }
        if (paramPacket instanceof PackageCloudPlayerConnect){
            Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerConnectEvent(((PackageCloudPlayerConnect) paramPacket).getName(), ((PackageCloudPlayerConnect) paramPacket).getProxy(), ((PackageCloudPlayerConnect) paramPacket).getUuid()));
        }

    }

    @Override
    public void onConnect(ChannelHandlerContext paramChannelHandlerContext) {

    }

    @Override
    public void onDisconnect(ChannelHandlerContext paramChannelHandlerContext) {

    }
}
