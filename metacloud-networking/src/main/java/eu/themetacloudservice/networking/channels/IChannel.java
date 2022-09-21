package eu.themetacloudservice.networking.channels;

import eu.themetacloudservice.networking.protocol.ISender;
import eu.themetacloudservice.networking.protocol.Packet;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;

public interface IChannel extends Closeable, ISender {
    @Override
    void close() throws IOException;

    @Override
    void sendPacket(Packet packet);

    boolean isConnected();

    ChannelPipeline getPipeline();

    SocketAddress getRemoteAddress();

    InetAddress getLocalAddress();
}
