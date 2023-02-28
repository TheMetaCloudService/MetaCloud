package eu.metacloudservice.networking;

import eu.metacloudservice.networking.client.NettyClient;
import eu.metacloudservice.networking.packet.PacketDriver;
import eu.metacloudservice.networking.server.NettyServer;

import java.util.ArrayList;

public class NettyDriver {
    private static NettyDriver instance;

    public NettyServer nettyServer;

    public ArrayList<String> whitelist;

    public NettyClient nettyClient;
    public PacketDriver packetDriver;

    public NettyDriver() {
        instance = this;
        packetDriver = new PacketDriver();
        whitelist = new ArrayList<>();
    }

    public static NettyDriver getInstance() {
        return instance;
    }
}
