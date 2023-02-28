package eu.metacloudservice.networking.packet;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class PacketDriver {


    private final HashMap<Integer, Class<? extends Packet>> packets;
    private final Map<Integer, NettyAdaptor> adaptor;

    public PacketDriver() {
        packets = new HashMap<>();
        adaptor = new HashMap<>();
    }


    public Map<Integer, NettyAdaptor> getAdaptor() {
        return adaptor;
    }

    public boolean handle(Integer id, Channel channelFuture, Packet packet){
        if (adaptor.containsKey(id)){
            adaptor.get(id).handle(channelFuture, packet);
            return true;
        }else {
            return false;
        }
    }
    public PacketDriver registerHandler(Integer id, NettyAdaptor nettyAdaptor, Class<? extends Packet> pc){
        this.adaptor.put(id, nettyAdaptor);
        try {
            this.packets.put(pc.newInstance().getPacketUUID(), pc);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;

    }
    public PacketDriver registerPacket(Class<? extends Packet> pc) {
        try {
            this.packets.put(pc.newInstance().getPacketUUID(), pc);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }
    public Class<? extends Packet> getPacket(int id) {
        return this.packets.get(id);
    }

}
