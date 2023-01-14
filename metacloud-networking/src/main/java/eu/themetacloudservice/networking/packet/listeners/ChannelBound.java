package eu.themetacloudservice.networking.packet;

import eu.themetacloudservice.networking.NettyDriver;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChannelBound extends SimpleChannelInboundHandler<Packet> {
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        (NettyDriver.getInstance()).packetDriver.executeListeners(ListenerType.RECEIVE, ctx, packet);
    }

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        (NettyDriver.getInstance()).packetDriver.executeListeners(ListenerType.CONNECT, ctx, null);
    }

    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        (NettyDriver.getInstance()).packetDriver.executeListeners(ListenerType.DISCONNECT, ctx, null);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    return;
    }

}
