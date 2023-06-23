package eu.metacloudservice.networking.packet;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PacketDriver {

    private final Map<Integer, Class<? extends Packet>> packets;
    private final Map<Integer, NettyAdaptor> adaptor;

    public PacketDriver() {
        packets = new ConcurrentHashMap<>();
        adaptor = new ConcurrentHashMap<>();
    }

    public Map<Integer, NettyAdaptor> getAdaptor() {
        return adaptor;
    }

    public boolean handle(Integer id, Channel channelFuture, Packet packet) {
        NettyAdaptor nettyAdaptor = adaptor.get(id);
        if (nettyAdaptor != null) {
            nettyAdaptor.handle(channelFuture, packet);
            return true;
        } else {
            return false;
        }
    }

    public PacketDriver registerHandler(Integer id, NettyAdaptor nettyAdaptor, Class<? extends Packet> pc) {
        this.adaptor.putIfAbsent(id, nettyAdaptor);
        try {
            this.packets.putIfAbsent(pc.newInstance().getPacketUUID(), pc);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Class<? extends Packet> getPacket(int id) {
        return this.packets.getOrDefault(id, null);
    }
}
