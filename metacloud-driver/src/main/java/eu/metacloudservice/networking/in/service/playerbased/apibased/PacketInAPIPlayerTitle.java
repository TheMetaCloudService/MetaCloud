package eu.metacloudservice.networking.in.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

public class PacketInAPIPlayerTitle extends Packet {
    @Getter
    private String title;
    @Getter
    private String subTitle;
    @Getter
    private int fadeIn;
    @Getter
    private int stay;
    @Getter
    private int fadeOut;
    @Getter
    private String username;

    public PacketInAPIPlayerTitle() {
        setPacketUUID(2345621);
    }

    public PacketInAPIPlayerTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut, String username) {
        setPacketUUID(2345621);
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        this.username = username;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        title = buffer.readString();
        subTitle = buffer.readString();
        fadeIn = buffer.readInt();
        stay = buffer.readInt();
        fadeOut = buffer.readInt();
        username = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(title);
        buffer.writeString(subTitle);
        buffer.writeInt(fadeIn);
        buffer.writeInt(stay);
        buffer.writeInt(fadeOut);
        buffer.writeString(username);
    }
}
