package eu.metacloudservice.networking.codec;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.var;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder  {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {


        try {
            int id = byteBuf.readInt();
            var packetClass = NettyDriver.getInstance().packetDriver.getPacket(id);
            if(packetClass != null) {
                Packet packet = packetClass.newInstance();
                packet.readPacket(new NettyBuffer(byteBuf));
                list.add(packet);
                NettyDriver.getInstance().packetDriver.handle(id, channelHandlerContext.channel(), packet);
            }
        }catch (Exception ignored){
            ignored.printStackTrace();
        }
    }
}
