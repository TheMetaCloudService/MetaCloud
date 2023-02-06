package eu.themetacloudservice.manager.networking;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.events.dummys.processbased.ServiceConnectedEvent;
import eu.themetacloudservice.events.dummys.processbased.ServiceLaunchEvent;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.manager.cloudservices.entry.TaskedService;
import eu.themetacloudservice.manager.cloudservices.enums.TaskedServiceStatus;
import eu.themetacloudservice.network.autentic.PackageAuthenticByManager;
import eu.themetacloudservice.network.autentic.PackageAuthenticRequestFromManager;
import eu.themetacloudservice.network.autentic.PackageCallBackAuthenticByManager;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceExit;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceLaunch;
import eu.themetacloudservice.network.nodes.from.PackageToManagerHandelNodeShutdown;
import eu.themetacloudservice.network.service.PackageConnectedServiceToALL;
import eu.themetacloudservice.network.service.proxyconnect.PackageConnectedProxyCallBack;
import eu.themetacloudservice.network.service.proxyconnect.PackageConnectedProxyCallBackData;
import eu.themetacloudservice.network.service.proxyconnect.Service;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.terminal.enums.Type;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.function.Consumer;


public class ManagerNetworkChannel implements IPacketListener {

    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {
        if (paramChannelHandlerContext == null) return;
        if (paramPacket == null) return;
        if (paramPacket instanceof PackageAuthenticByManager){
            PackageAuthenticByManager pack = (PackageAuthenticByManager) paramPacket;
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);

            if (pack.isNode()){
                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der node '§f"+pack.getAuthenticName()+"§r' versucht, eine Verbindung zur Cloud herzustellen",
                        "the node '§f"+pack.getAuthenticName()+"§r' tries to connect to the cloud");

                if (config.getNodes().stream().noneMatch(managerConfigNodes -> managerConfigNodes.getName().equals(pack.getAuthenticName()))){

                    PackageCallBackAuthenticByManager packet = new PackageCallBackAuthenticByManager();
                    packet.setAccepted(false);
                    packet.setReason("the node was not found! error: '404'");
                    paramChannelHandlerContext.channel().writeAndFlush(packet);
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der angegebene Node wurde nicht gefunden, die Verbindung wird getrennt",
                            "the specified node was not found, disconnect the connection");

                    return;
                }
                if (!Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey()).equals(pack.getConnectionKey())){
                    PackageCallBackAuthenticByManager packet = new PackageCallBackAuthenticByManager();
                    packet.setAccepted(false);
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Verbindungsschlüssel ist nicht korrekt, die Verbindung wird unterbrochen",
                            "the connectionkey is not correct, the connection is disconnected");
                    packet.setReason("the connectionkey is not correct, please check it error: '405'");
                    paramChannelHandlerContext.channel().writeAndFlush(packet);
                    return;
                }
                if (NettyDriver.getInstance().nettyServer.isChannelFound(pack.getAuthenticName())){
                    PackageCallBackAuthenticByManager packet = new PackageCallBackAuthenticByManager();
                    packet.setAccepted(false);
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Node ist bereits verbunden und kann nicht 2 verbindungen haben",
                            "The node is already connected and cannot have 2 connections");
                    packet.setReason("Already connected to Cloud '407'");
                    paramChannelHandlerContext.channel().writeAndFlush(packet);
                    return;
                }
                NettyDriver.getInstance().nettyServer.registerChannel(pack.getAuthenticName(), paramChannelHandlerContext.channel());

                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Node '§f"+pack.getAuthenticName()+"§r' ist nun erfolgreich verbunden, es können nun alle Tasks gestartet werden",
                        "the node '§f"+pack.getAuthenticName()+"§r' is now successfully connected, all tasks can now be started");
                PackageCallBackAuthenticByManager packet = new PackageCallBackAuthenticByManager();
                packet.setReason("");
                packet.setAccepted(true);
                NettyDriver.getInstance().nettyServer.sendPacket(pack.getAuthenticName(), packet);

            }else {
                if (!Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey()).equals(pack.getConnectionKey())){
                    PackageCallBackAuthenticByManager packet = new PackageCallBackAuthenticByManager();
                    packet.setAccepted(false);
                    packet.setReason("the connectionkey is not correct, please check it error: '405'");
                    paramChannelHandlerContext.channel().writeAndFlush(packet);
                    return;
                }else if ( CloudManager.serviceDriver.getService(pack.getAuthenticName()) == null){
                    PackageCallBackAuthenticByManager packet = new PackageCallBackAuthenticByManager();
                    packet.setAccepted(false);
                    packet.setReason("Not found '404'");
                    paramChannelHandlerContext.channel().writeAndFlush(packet);
                }
                else if (NettyDriver.getInstance().nettyServer.isChannelFound(pack.getAuthenticName())){
                    PackageCallBackAuthenticByManager packet = new PackageCallBackAuthenticByManager();
                    packet.setAccepted(false);
                    packet.setReason("Already connected to Cloud '407'");
                    paramChannelHandlerContext.channel().writeAndFlush(packet);
                    return;
                }else {
                    NettyDriver.getInstance().nettyServer.registerChannel(pack.getAuthenticName(), paramChannelHandlerContext.channel());
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Service '§f"+pack.getAuthenticName()+"§r' ist nun §ferfolgreich§r verbunden",
                            "the service '§f"+pack.getAuthenticName()+"§r' is now §fsuccessfully§r connected");
                    CloudManager.serviceDriver.getService(pack.getAuthenticName()).handelStatusChange(TaskedServiceStatus.LOBBY);

                    if (Driver.getInstance().getGroupDriver().load(pack.getAuthenticName().split(config.getSplitter())[0]).getGroupType().equals("PROXY")){



                        CloudManager.serviceDriver.getServices().forEach(taskedService -> {

                            if (taskedService.getEntry().getStatus() != TaskedServiceStatus.IN_GAME || taskedService.getEntry().getStatus() != TaskedServiceStatus.LOBBY) return;
                            Group group = Driver.getInstance().getGroupDriver().load(taskedService.getEntry().getGroupName());
                            String address = "";
                            if (group.getStorage().getRunningNode().equals("InternalNode")){
                                address = "127.0.0.1";
                            }else {
                                address = config.getNodes().stream().filter(managerConfigNodes -> managerConfigNodes.getName().equals(group.getStorage().getRunningNode())).findFirst().get().getAddress();
                            }

                                    PackageConnectedServiceToALL packageConnectedServiceToALL = new PackageConnectedServiceToALL(taskedService.getEntry().getServiceName(),
                            taskedService.getEntry().getGroupName(),taskedService.getEntry().getUsedPort(), taskedService.getEntry().getNode(), address, group.getGroupType(), new ConfigDriver().convert(group));

                            NettyDriver.getInstance().nettyServer.sendToAllPackets(packageConnectedServiceToALL);
                        });

                        Group group = Driver.getInstance().getGroupDriver().load(pack.getAuthenticName().split(config.getSplitter())[0]);

                        String address = "";
                        if (group.getStorage().getRunningNode().equals("InternalNode")){
                            address = "127.0.0.1";
                        }else {
                            address = config.getNodes().stream().filter(managerConfigNodes -> managerConfigNodes.getName().equals(group.getStorage().getRunningNode())).findFirst().get().getAddress();
                        }


                        Driver.getInstance().getEventDriver().executeEvent(new ServiceConnectedEvent(pack.getAuthenticName(), group.getStorage().getRunningNode(), CloudManager.serviceDriver.getService(pack.getAuthenticName()).getEntry().getUsedPort(), address, group.getGroup()));
                        PackageConnectedServiceToALL packageConnectedServiceToALL = new PackageConnectedServiceToALL(pack.getAuthenticName(), CloudManager.serviceDriver.getService(pack.getAuthenticName()).getEntry().getGroupName(), CloudManager.serviceDriver.getService(pack.getAuthenticName()).getEntry().getUsedPort(), CloudManager.serviceDriver.getService(pack.getAuthenticName()).getEntry().getNode(), address, group.getGroupType(), new ConfigDriver().convert(group));

                        NettyDriver.getInstance().nettyServer.sendToAllPackets(packageConnectedServiceToALL);

                    }else {
                        Group group = Driver.getInstance().getGroupDriver().load(pack.getAuthenticName().split(config.getSplitter())[0]);
                        String address = "";
                        if (group.getStorage().getRunningNode().equals("InternalNode")){
                            address = "127.0.0.1";
                        }else {
                            address = config.getNodes().stream().filter(managerConfigNodes -> managerConfigNodes.getName().equals(group.getStorage().getRunningNode())).findFirst().get().getAddress();
                        }

                        Driver.getInstance().getEventDriver().executeEvent(new ServiceConnectedEvent(pack.getAuthenticName(), group.getStorage().getRunningNode(), CloudManager.serviceDriver.getService(pack.getAuthenticName()).getEntry().getUsedPort(), address, group.getGroup()));

                        PackageConnectedServiceToALL packageConnectedServiceToALL = new PackageConnectedServiceToALL(pack.getAuthenticName(), CloudManager.serviceDriver.getService(pack.getAuthenticName()).getEntry().getGroupName(), CloudManager.serviceDriver.getService(pack.getAuthenticName()).getEntry().getUsedPort(), CloudManager.serviceDriver.getService(pack.getAuthenticName()).getEntry().getNode(), address, group.getGroupType(), new ConfigDriver().convert(group));

                        NettyDriver.getInstance().nettyServer.sendToAllPackets(packageConnectedServiceToALL);
                    }

                    PackageCallBackAuthenticByManager packet = new PackageCallBackAuthenticByManager();
                    packet.setAccepted(true);
                    paramChannelHandlerContext.channel().writeAndFlush(packet);
                }
            }

        }if (paramPacket instanceof PackageToManagerHandelNodeShutdown){
            PackageToManagerHandelNodeShutdown pack = (PackageToManagerHandelNodeShutdown)paramPacket;
            CloudManager.serviceDriver.getServicesFromNode(pack.getNode()).forEach(taskedService -> {
                CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName());
            });
            NettyDriver.getInstance().nettyServer.removeChannel(pack.getNode());
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Node '§f"+pack.getNode()+"§r' ist jetzt offline, alle Dienste wurden gelöscht", "the node '§f"+pack.getNode()+"§r' is now offline, all services have been deleted");
        }if (paramPacket instanceof PackageToManagerCallBackServiceLaunch){
            PackageToManagerCallBackServiceLaunch launch = (PackageToManagerCallBackServiceLaunch)paramPacket;
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Service '§f"+launch.getService()+"§r' wird gestartet 'node: §f"+launch.getNode()+"§r, port: §f"+launch.getPort()+"§r'",
                    "The service '§f"+launch.getService()+"§r' is starting 'node: §f"+launch.getNode()+"§r, port: §f"+launch.getPort()+"§r'");
            CloudManager.serviceDriver.getService(launch.getService()).getEntry().setUsedPort(launch.getPort());

        }if (paramPacket instanceof PackageToManagerCallBackServiceExit){
            PackageToManagerCallBackServiceExit launch = (PackageToManagerCallBackServiceExit)paramPacket;
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Service '§f"+launch.getService()+"§r' wird angehalten",
                    "The service '§f"+launch.getService()+"§r' is stopping");
        }
    }

    @Override
    public void onConnect(ChannelHandlerContext paramChannelHandlerContext) {
        if (paramChannelHandlerContext == null) return;

        PackageAuthenticRequestFromManager packet = new PackageAuthenticRequestFromManager();
        paramChannelHandlerContext.channel().writeAndFlush(packet);
    }

    @Override
    public void onDisconnect(ChannelHandlerContext paramChannelHandlerContext) {
    }
}
