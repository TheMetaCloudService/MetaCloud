/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PacketOutAPIPlayerTitle extends Packet {

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

    public PacketOutAPIPlayerTitle() {
        setPacketUUID(234112111);
    }

    public PacketOutAPIPlayerTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut, String username) {
        setPacketUUID(234112111);
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        this.username = username;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {
        title = buffer.readString();
        subTitle = buffer.readString();
        fadeIn = buffer.readInt();
        stay = buffer.readInt();
        fadeOut = buffer.readInt();
        username = buffer.readString();
    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(title);
        buffer.writeString(subTitle);
        buffer.writeInt(fadeIn);
        buffer.writeInt(stay);
        buffer.writeInt(fadeOut);
        buffer.writeString(username);
    }
}
