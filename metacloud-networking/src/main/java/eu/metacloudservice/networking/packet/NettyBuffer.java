package eu.metacloudservice.networking.packet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class NettyBuffer {

    private final ByteBuf byteBuf;
    protected static final Gson GSON = (new GsonBuilder()).serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();

    public NettyBuffer(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public void writeString(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        byteBuf.ensureWritable(8 + bytes.length);
        try {
            byteBuf.writeLong(bytes.length);
            byteBuf.writeBytes(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String readString() {
        if (byteBuf.readableBytes() < 8) {
            throw new IllegalStateException("Not enough data to read message length (byteBuf.readableBytes() < 8): " + byteBuf.readableBytes());
        }
        long messageLength = byteBuf.readLong();
        if (byteBuf.readableBytes() < messageLength) {
            throw new IllegalStateException("Not enough data to read message length (byteBuf.readableBytes() < messageLength): " + byteBuf.readableBytes());
        }
        byte[] bytes = new byte[(int) messageLength];
        byteBuf.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
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

    public void writeClass(Object o){
        writeString( GSON.toJson(o));
    }

    @SneakyThrows
    public Object readClass(Class<?> c){
        String read = readString();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(read, c);
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


    @SneakyThrows
    public void writeMap(HashMap map){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);
        byte[] bytes = baos.toByteArray();
        byteBuf.writeBytes(bytes);

    }

    @SneakyThrows
    public HashMap readMap(){
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (HashMap) ois.readObject();
    }

    public void writeList(ArrayList list){
        byteBuf.writeLong(list.size() * 2);
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
