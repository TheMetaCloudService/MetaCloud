package eu.metacloudservice.networking;

import eu.metacloudservice.networking.client.NettyClient;
import eu.metacloudservice.networking.packet.PacketDriver;
import eu.metacloudservice.networking.server.NettyServer;

public class NettyDriver {
    private static NettyDriver instance;

    public NettyServer nettyServer;

    public NettyClient nettyClient;
    public PacketDriver packetDriver;

    public NettyDriver() {
        instance = this;
        packetDriver = new PacketDriver();
    }

    public static NettyDriver getInstance() {
        return instance;
    }
}
