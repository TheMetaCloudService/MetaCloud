package eu.metacloudservice.networking.out.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import lombok.Getter;

import java.util.ArrayList;

public class PacketOutAPIPlayerComponent extends Packet {


    @Getter
    private String username;
    @Getter
    private String component;

    @Getter
    private String clickEventAction;

    @Getter
    private String clickResul;

    @Getter
    private String hoverEventAction;

    @Getter
    private String hoverResul;

    @Getter
    private ArrayList<String> extras;

    public PacketOutAPIPlayerComponent() {
        setPacketUUID(3238832);
    }

    public PacketOutAPIPlayerComponent(String username, String component, String clickEventAction, String clickResul, String hoverEventAction, String hoverResul, ArrayList<String> extras) {
        setPacketUUID(3238832);
        this.username = username;
        this.component = component;
        this.clickEventAction = clickEventAction;
        this.clickResul = clickResul;
        this.hoverEventAction = hoverEventAction;
        this.hoverResul = hoverResul;
        this.extras = extras;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        username = buffer.readString();
        component = buffer.readString();
        clickEventAction = buffer.readString();
        clickResul = buffer.readString();
        hoverEventAction = buffer.readString();
        hoverResul = buffer.readString();
        extras = buffer.readList();

    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(username);
        buffer.writeString(component);
        buffer.writeString(clickEventAction);
        buffer.writeString(clickResul);
        buffer.writeString(hoverEventAction);
        buffer.writeString(hoverResul);
        buffer.writeList(extras);
    }
}
