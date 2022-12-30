package eu.themetacloudservice.networking;

import eu.themetacloudservice.networking.client.NettyClient;
import eu.themetacloudservice.networking.packet.PacketDriver;
import eu.themetacloudservice.networking.server.NettyServer;

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
