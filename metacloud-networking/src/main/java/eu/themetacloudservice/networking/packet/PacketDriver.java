package eu.themetacloudservice.networking.packet;

import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.networking.packet.listeners.ListenerType;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PacketDriver {


    private final HashMap<Integer, Class<? extends Packet>> packets;

    private final ArrayList<IPacketListener> listeners ;

    public PacketDriver() {
        listeners = new ArrayList<>();
        packets = new HashMap<>();
    }

    public void executeListeners(ListenerType type, ChannelHandlerContext chx, Packet packet) {
        if (type == ListenerType.CONNECT)
            this.listeners.forEach(iPacketListener -> iPacketListener.onConnect(chx));
        if (type == ListenerType.DISCONNECT)
            this.listeners.forEach(iPacketListener -> iPacketListener.onDisconnect(chx));
        if (type == ListenerType.RECEIVE)
            this.listeners.forEach(iPacketListener -> iPacketListener.onReceive(chx, packet));
    }


    public PacketDriver handelListener(IPacketListener ipl) {
        this.listeners.add(ipl);
        return this;
    }

    public PacketDriver handelPacket(Class<? extends Packet> pc) {
        try {
            this.packets.put(pc.newInstance().getPacketUUID(), pc);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Integer getIDFromPacket(Packet packet) {
        for (Iterator<Integer> iterator = this.packets.keySet().iterator(); iterator.hasNext(); ) {
            int id = iterator.next();
            if (packet.getClass().equals(this.packets.get(id)))
                return id;
        }
        return -1;
    }

    public Class<? extends Packet> getPacketFromId(int id) {
        return this.packets.get(id);
    }

}
