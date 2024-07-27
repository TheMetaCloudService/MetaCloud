package eu.metacloudservice.networking.packet;

import io.netty.channel.Channel;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PacketDriver {

    private final Map<Integer, Class<? extends Packet>> packets = new ConcurrentHashMap<>();
    private final Map<Integer, NettyAdaptor> adaptor = new ConcurrentHashMap<>();

    @SneakyThrows
    public PacketDriver() {}

    public void call(@NonNull final Integer id, @NonNull final Channel channel, @NonNull final Packet packet) {
        final NettyAdaptor nettyAdaptor = adaptor.get(id);
        if (nettyAdaptor != null) {
            nettyAdaptor.handle(channel, packet);
        }
    }

    public PacketDriver registerHandler(@NonNull final Integer id ,@NonNull final NettyAdaptor nettyAdaptor, @NonNull final Class<? extends Packet> pc) {
        adaptor.putIfAbsent(id, nettyAdaptor);
        try {
           final Packet packetInstance = pc.getDeclaredConstructor().newInstance();
            packets.putIfAbsent(packetInstance.getPacketUUID(), pc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Class<? extends Packet> getPacket(final int id) {
        return packets.getOrDefault(id, null);
    }
}


