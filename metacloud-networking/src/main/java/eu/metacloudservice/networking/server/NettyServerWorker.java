/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.server;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyServerWorker extends SimpleChannelInboundHandler<Packet> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        NettyDriver.getInstance().getPacketDriver().call(packet.getPacketUUID(), channelHandlerContext.channel(), packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
