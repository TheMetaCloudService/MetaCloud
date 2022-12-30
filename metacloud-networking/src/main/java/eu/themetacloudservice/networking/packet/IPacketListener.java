package eu.themetacloudservice.networking.packet;

import io.netty.channel.ChannelHandlerContext;

public interface IPacketListener {
    void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket);

    void onConnect(ChannelHandlerContext paramChannelHandlerContext);

    void onDisconnect(ChannelHandlerContext paramChannelHandlerContext);
}
