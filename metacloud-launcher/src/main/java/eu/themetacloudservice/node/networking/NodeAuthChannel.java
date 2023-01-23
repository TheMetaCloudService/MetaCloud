package eu.themetacloudservice.node.networking;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.network.autentic.PackageAuthenticByManager;
import eu.themetacloudservice.network.autentic.PackageAuthenticRequestFromManager;
import eu.themetacloudservice.network.autentic.PackageCallBackAuthenticByManager;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.terminal.enums.Type;
import io.netty.channel.ChannelHandlerContext;

public class NodeNetworkChannel implements IPacketListener {

    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {
        if (paramChannelHandlerContext == null) return;
        if (paramPacket == null) return;
        if (paramPacket instanceof PackageAuthenticRequestFromManager){

            NettyDriver.getInstance().nettyClient.setManager(paramChannelHandlerContext.channel());
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "der Manager benötigt eine Authentifizierung, um diesem Client zu vertrauen", "the mannager requires authentication to trust this client");
            AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
            NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
            PackageAuthenticByManager packet = new PackageAuthenticByManager();
            packet.setAuthenticName(config.getNodeName());
            packet.setConnectionKey(Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey()));
            packet.setNode(true);
            NettyDriver.getInstance().nettyClient.sendPacket(packet);
        } else if (paramPacket instanceof PackageCallBackAuthenticByManager){
            PackageCallBackAuthenticByManager packet = (PackageCallBackAuthenticByManager) paramPacket;
            if (packet.isAccepted()){
                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Authentifizierung war erfolgreich, Warten auf neue Aufgaben",
                        "authentication was successful, waiting for new tasks");
            }else {
                System.exit(0);
            }
        }

    }



    @Override
    public void onConnect(ChannelHandlerContext paramChannelHandlerContext) {
        if (paramChannelHandlerContext == null) return;

    }

    @Override
    public void onDisconnect(ChannelHandlerContext paramChannelHandlerContext) {
        if (paramChannelHandlerContext == null) return;
    }
}
