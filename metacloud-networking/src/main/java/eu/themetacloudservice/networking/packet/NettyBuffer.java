package eu.themetacloudservice.networking.packet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Consumer;

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


    public void writeList(ArrayList list){
        byteBuf.writeInt(list.size()*2);
        list.forEach(o -> {
            if (o instanceof String){
                writeString("STRING");
                writeString(String.valueOf(o));
            }else if (o instanceof Integer){
                writeString("INT");
                writeInt(Integer.valueOf(String.valueOf(o)));
            }else if (o instanceof Boolean){
                writeString("BOOLEAN");
                writeBoolean(Boolean.valueOf(String.valueOf(o)));
            }else if (o instanceof Long){
                writeString("LONG");
                writeLong(Long.valueOf(String.valueOf(o)));
            }else if (o instanceof Double){
                writeString("DOUBLE");
                writeDouble(Double.valueOf(String.valueOf(o)));
            }
        });
    }

    public ArrayList readList(){
        ArrayList list = new ArrayList();
        int size = readInt();
        String lastType = "";
        Boolean typeGiven = false;
        for (int i = 0; i != size ; i++) {
            if (!typeGiven){
                String type = readString();
                lastType = type;
                typeGiven = true;
            }else {
                if (lastType.equals("STRING")) {
                    list.add(readString());
                    typeGiven = false;
                }
                if (lastType.equals("INT")) {
                    list.add(readInt());
                    typeGiven = false;
                }
                if (lastType.equals("BOOLEAN")) {
                    list.add(readBoolean());
                    typeGiven = false;
                }
                if (lastType.equals("LONG")) {
                    list.add(readLong());
                    typeGiven = false;
                }
                if (lastType.equals("DOUBLE")) {
                    list.add(readDouble());
                    typeGiven = false;
                }
            }
        }
        return list;
     }

}
