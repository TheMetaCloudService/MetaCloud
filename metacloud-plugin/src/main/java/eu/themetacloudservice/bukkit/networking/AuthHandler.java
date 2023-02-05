package eu.themetacloudservice.bukkit.networking;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.themetacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.themetacloudservice.network.autentic.PackageAuthenticByManager;
import eu.themetacloudservice.network.autentic.PackageAuthenticRequestFromManager;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import io.netty.channel.ChannelHandlerContext;

public class AuthHandler implements IPacketListener {
    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {


        if(paramPacket instanceof PackageAuthenticRequestFromManager){
            NettyDriver.getInstance().nettyClient.setManager(paramChannelHandlerContext.channel());
            AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
            LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
            PackageAuthenticByManager packet = new PackageAuthenticByManager();
            packet.setNode(false);
            packet.setAuthenticName(service.getService());
            packet.setConnectionKey(Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey()));
            paramChannelHandlerContext.channel().writeAndFlush(packet);

        }
    }

    @Override
    public void onConnect(ChannelHandlerContext paramChannelHandlerContext) {

    }

    @Override
    public void onDisconnect(ChannelHandlerContext paramChannelHandlerContext) {

    }
}
