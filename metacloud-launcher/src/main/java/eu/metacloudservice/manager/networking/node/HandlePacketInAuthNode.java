package eu.metacloudservice.manager.networking.node;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.networking.in.node.PacketInAuthNode;
import eu.metacloudservice.networking.out.node.PacketOutAuthSuccess;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.terminal.enums.Type;
import io.netty.channel.Channel;

public class HandlePacketInAuthNode implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInAuthNode){
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der node '§f"+((PacketInAuthNode) packet).getNode()+"§r' versucht, eine Verbindung zur Cloud herzustellen",
                    "the node '§f"+((PacketInAuthNode) packet).getNode()+"§r' tries to connect to the cloud");
            if (config.getNodes().parallelStream().noneMatch(managerConfigNodes -> managerConfigNodes.getName().equals(((PacketInAuthNode) packet).getNode()))){
                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der angegebene Node wurde nicht gefunden, die Verbindung wird getrennt",
                        "the specified node was not found, disconnect the connection");
                channel.disconnect();
            }else if (!Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey()).equals(((PacketInAuthNode) packet).getKey())){
                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Verbindungsschlüssel ist nicht korrekt, die Verbindung wird unterbrochen",
                        "the connectionkey is not correct, the connection is disconnected");
                channel.disconnect();
            }else if (NettyDriver.getInstance().nettyServer.isChannelFound(((PacketInAuthNode) packet).getNode())){
                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Node ist bereits verbunden und kann nicht 2 verbindungen haben",
                        "The node is already connected and cannot have 2 connections");
                channel.disconnect();
            }else {
                NettyDriver.getInstance().nettyServer.registerChannel(((PacketInAuthNode) packet).getNode(), channel);
                NettyDriver.getInstance().nettyServer.sendPacketAsynchronous(((PacketInAuthNode) packet).getNode(), new PacketOutAuthSuccess());

                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Node '§f"+((PacketInAuthNode) packet).getNode()+"§r' ist nun erfolgreich verbunden, es können nun alle Tasks gestartet werden",
                        "the node '§f"+((PacketInAuthNode) packet).getNode()+"§r' is now successfully connected, all tasks can now be started");

            }

        }
    }
}
