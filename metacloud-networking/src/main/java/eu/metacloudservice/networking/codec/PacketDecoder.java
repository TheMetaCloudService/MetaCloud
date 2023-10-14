package eu.metacloudservice.networking.codec;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.SneakyThrows;
import lombok.var;

import javax.naming.spi.DirObjectFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    @SneakyThrows
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)  {
         if (byteBuf.readableBytes() < 4 ){
            return;
        }else {
                int id = byteBuf.readInt();
                if (id == 0) return;
                var packetClass = NettyDriver.getInstance().getPacketDriver().getPacket(id);
                if (packetClass != null) {
                    Packet packet = null;
                    packet = packetClass.getDeclaredConstructor().newInstance();
                    packet.setPacketUUID(id); // Setze die UUID manuell
                    packet.readPacket(new NettyBuffer(byteBuf));
                    list.add(packet);
                    NettyDriver.getInstance().getPacketDriver().handle(id,channelHandlerContext.channel(), packet);
                }else {
                    return;
                }
        }

    }
}
