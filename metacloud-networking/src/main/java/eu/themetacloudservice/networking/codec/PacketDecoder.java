package eu.themetacloudservice.networking.codec;

import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.var;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder  {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int id = byteBuf.readInt();
        var packetClass = NettyDriver.getInstance().packetDriver.getPacketFromId(id);
        if(packetClass != null) {
            Packet packet = packetClass.newInstance();
            packet.readPacket(new NettyBuffer(byteBuf));
            list.add(packet);
        } else {
            System.out.println("Invalid packet: " + id);
        }
    }
}
