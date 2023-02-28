package eu.metacloudservice.networking.codec;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) {

        try {
            byteBuf.writeInt(packet.getPacketUUID());
            packet.writePacket(new NettyBuffer(byteBuf));
        }catch (Exception ignored){}
    }
}
