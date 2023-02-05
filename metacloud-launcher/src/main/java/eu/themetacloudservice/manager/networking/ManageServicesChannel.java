package eu.themetacloudservice.manager.networking;

import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.network.service.PackageServiceShutdown;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import io.netty.channel.ChannelHandlerContext;

public class ManageServicesChannel implements IPacketListener {
    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {

        if (paramPacket instanceof PackageServiceShutdown){
            CloudManager.serviceDriver.unregister(((PackageServiceShutdown) paramPacket).getName());
        }
    }

    @Override
    public void onConnect(ChannelHandlerContext paramChannelHandlerContext) {

    }

    @Override
    public void onDisconnect(ChannelHandlerContext paramChannelHandlerContext) {

    }
}
