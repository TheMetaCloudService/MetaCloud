package eu.themetacloudservice.networking;

import eu.themetacloudservice.networking.channels.IChannelInitializer;
import eu.themetacloudservice.networking.protocol.ISender;
import eu.themetacloudservice.networking.protocol.Packet;
import eu.themetacloudservice.networking.worker.Options;

import java.io.Closeable;
import java.io.IOException;

public interface Structure extends Closeable, ISender {

    @Override
    void close() throws IOException;

    @Override
    void sendPacket(Packet packet);

    Structure init(IChannelInitializer initializer);

    Structure option(Options<?> option, Object value);

    <T> T getOption(Options<T> option);

    boolean isConnected();
}
