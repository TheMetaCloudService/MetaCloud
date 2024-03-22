/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.var;

public class PacketLengthSerializer extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) throws Exception {
        try {
            final var readable = byteBuf.readableBytes();

            byteBuf2.ensureWritable(readable + this.getVarIntSize(readable));
            writeVarInt(byteBuf2, readable);
            byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), readable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeVarInt(final ByteBuf buf, int value) {
        while (true) {
            if ((value & -128) == 0) {
                buf.writeByte(value);
                return;
            }

            buf.writeByte(value & 127 | 128);
            value >>>= 7;
        }
    }

    private int getVarIntSize(final int value) {
        if ((value & 0xffffff80) == 0) {
            return 1;
        } else if ((value & 0xffffc000) == 0) {
            return 2;
        } else if ((value & 0xffe00000) == 0) {
            return 3;
        } else if ((value & 0xf0000000) == 0) {
            return 4;
        } else {
            return 5;
        }
    }

}
