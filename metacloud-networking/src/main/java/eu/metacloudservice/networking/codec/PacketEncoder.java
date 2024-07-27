package eu.metacloudservice.networking.codec;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        final NettyBuffer nettyBuffer = new NettyBuffer(out);
        int packetUUID = packet.getPacketUUID();
        out.writeInt(packetUUID);
        packet.writePacket(nettyBuffer);

    }
}