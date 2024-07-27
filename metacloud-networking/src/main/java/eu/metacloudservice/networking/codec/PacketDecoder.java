package eu.metacloudservice.networking.codec;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.SneakyThrows;
import lombok.var;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    @SneakyThrows
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            return;
        }
        final int packetUUID = in.readInt();
       final var packetClass = NettyDriver.getInstance().getPacketDriver().getPacket(packetUUID);
        if (packetClass != null) {
            try {
                final var packet = packetClass.getDeclaredConstructor().newInstance();
                packet.readPacket(new NettyBuffer(in));
                out.add(packet);
            }catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception){
                exception.printStackTrace();
            }
        }
    }
}
