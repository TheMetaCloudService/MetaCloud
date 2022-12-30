package eu.themetacloudservice.networking.packet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class NettyBuffer {

    private ByteBuf byteBuf;

    public NettyBuffer(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public void writeString(String message){
        byteBuf.writeInt(message.length());
        byteBuf.writeCharSequence(message, StandardCharsets.UTF_8);
    }

    public String readString(){
        int messageLength = byteBuf.readInt();
        return byteBuf.readCharSequence(messageLength, StandardCharsets.UTF_8).toString();
    }

    public void writeInt(int integer) {
        byteBuf.writeInt(integer);
    }

    public int readInt() {
        return byteBuf.readInt();
    }

    public void writeLong(Long longs){
        byteBuf.writeLong(longs);
    }

    public Long readLong(){
       return byteBuf.readLong();
    }

    public void writeDouble(Double dub){
        byteBuf.writeDouble(dub);
    }


    public void writeFloat(float fla){
        byteBuf.writeFloat(fla);
    }

    public Float readFloat(){
        return byteBuf.readFloat();
    }

    public void writeChar(char cs){
        byteBuf.writeChar(cs);
    }

    public char readChar(){
        return byteBuf.readChar();
    }


    public Double readDouble(){
        return byteBuf.readDouble();
    }

    public void writeBoolean(boolean bool) {
        byteBuf.writeBoolean(bool);
    }
    public boolean readBoolean() {
        return byteBuf.readBoolean();
    }

}
