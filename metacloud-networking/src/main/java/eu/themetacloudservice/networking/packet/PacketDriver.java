package eu.themetacloudservice.networking.packet;

import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.networking.packet.listeners.ListenerType;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PacketDriver {


    private HashMap<Integer, Class<? extends Packet>> packets;

    private ArrayList<IPacketListener> listeners ;

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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Integer getIDFromPacket(Packet packet) {
        for (Iterator<Integer> iterator = this.packets.keySet().iterator(); iterator.hasNext(); ) {
            int id = ((Integer)iterator.next()).intValue();
            if (packet.getClass().equals(this.packets.get(Integer.valueOf(id))))
                return Integer.valueOf(id);
        }
        return Integer.valueOf(-1);
    }

    public Class<? extends Packet> getPacketFromId(int id) {
        return this.packets.get(Integer.valueOf(id));
    }

}
