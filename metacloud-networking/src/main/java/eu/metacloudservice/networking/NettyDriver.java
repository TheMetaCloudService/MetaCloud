package eu.metacloudservice.networking;

import eu.metacloudservice.networking.client.NettyClient;
import eu.metacloudservice.networking.packet.PacketDriver;
import eu.metacloudservice.networking.server.NettyServer;

import java.util.ArrayList;

public class NettyDriver {
    private static NettyDriver instance;

    public NettyServer nettyServer;

    private ArrayList<String> whitelist;

    public NettyClient nettyClient;
    private PacketDriver packetDriver;

    public NettyDriver() {
        instance = this;
        packetDriver = new PacketDriver();
        whitelist = new ArrayList<>();
    }

    public static NettyDriver getInstance() {
        return instance;
    }

    public ArrayList<String> getWhitelist() {
        return whitelist;
    }

    public PacketDriver getPacketDriver() {
        return packetDriver;
    }


}
