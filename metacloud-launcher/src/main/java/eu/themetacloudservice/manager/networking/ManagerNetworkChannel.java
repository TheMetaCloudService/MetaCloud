package eu.themetacloudservice.manager.networking;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.network.autentic.PackageAuthenticByManager;
import eu.themetacloudservice.network.autentic.PackageAuthenticRequestFromManager;
import eu.themetacloudservice.network.autentic.PackageCallBackAuthenticByManager;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceExit;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceLaunch;
import eu.themetacloudservice.network.nodes.from.PackageToManagerHandelNodeShutdown;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.terminal.enums.Type;
import io.netty.channel.ChannelHandlerContext;


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
