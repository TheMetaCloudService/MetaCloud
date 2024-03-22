/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.client;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;

public class NettyClientWorker extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, @NotNull Packet packet) throws Exception {
        NettyDriver.getInstance().getPacketDriver().call(packet.getPacketUUID(), channelHandlerContext.channel(), packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {}
}
