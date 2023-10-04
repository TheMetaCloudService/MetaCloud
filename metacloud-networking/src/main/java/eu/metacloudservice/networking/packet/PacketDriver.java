package eu.metacloudservice.networking.packet;

import eu.metacloudservice.networking.packet.NettyBuffer;
import io.netty.channel.Channel;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PacketDriver {

    private final Map<Integer, Class<? extends Packet>> packets = new ConcurrentHashMap<>();
    private final Map<Integer, NettyAdaptor> adaptor = new ConcurrentHashMap<>();



    @SneakyThrows
    public PacketDriver() {

    }

    public Map<Integer, NettyAdaptor> getAdaptor() {
        return adaptor;
    }

    public boolean handle(Integer id, Channel channel, Packet packet) {
        NettyAdaptor nettyAdaptor = adaptor.get(id);
        if (nettyAdaptor != null) {
            nettyAdaptor.handle(channel, packet);
            return true;
        }
        return false;
    }

    public PacketDriver registerHandler(Integer id, NettyAdaptor nettyAdaptor, Class<? extends Packet> pc) {
        adaptor.putIfAbsent(id, nettyAdaptor);
        try {
            Packet packetInstance = pc.getDeclaredConstructor().newInstance();
            packets.putIfAbsent(packetInstance.getPacketUUID(), pc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Class<? extends Packet> getPacket(int id) {
        return packets.getOrDefault(id, null);
    }
}


