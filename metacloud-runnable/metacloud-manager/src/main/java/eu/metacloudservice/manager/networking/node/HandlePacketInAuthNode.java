package eu.metacloudservice.manager.networking.node;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.in.node.PacketInAuthNode;
import eu.metacloudservice.networking.packet.packets.out.node.PacketOutAuthSuccess;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.terminal.enums.Type;
import io.netty.channel.Channel;

public class HandlePacketInAuthNode implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInAuthNode){

            AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
            Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-node-auth-request")
                    .replace("%node%", ((PacketInAuthNode) packet).getNode()));
            if (CloudManager.config.getNodes().parallelStream().noneMatch(managerConfigNodes -> managerConfigNodes.getName().equals(((PacketInAuthNode) packet).getNode()))){
                Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-node-auth-request-not-found"));
                channel.disconnect();
            }else if (!Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey()).equals(((PacketInAuthNode) packet).getKey())){
                Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-node-auth-request-key"));
                channel.disconnect();
            }else if (NettyDriver.getInstance().nettyServer.isChannelFound(((PacketInAuthNode) packet).getNode())){
                Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-node-auth-request-already-connected"));
                channel.disconnect();
            }else {
                NettyDriver.getInstance().nettyServer.registerChannel(((PacketInAuthNode) packet).getNode(), channel);
                NettyDriver.getInstance().nettyServer.sendPacketAsynchronous(((PacketInAuthNode) packet).getNode(), new PacketOutAuthSuccess());

                Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-node-auth-request-successful")
                        .replace("%node%", ((PacketInAuthNode) packet).getNode()));

            }

        }
    }
}
