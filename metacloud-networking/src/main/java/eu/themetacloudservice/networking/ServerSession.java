package eu.themetacloudservice.networking;

/*
 * Projectname: VapeCloud
 * Created AT: 28.12.2021/17:35
 * Created by Robin B. (RauchigesEtwas)
 */



import eu.themetacloudservice.networking.channels.IChannel;
import eu.themetacloudservice.networking.channels.IChannelInitializer;
import eu.themetacloudservice.networking.protocol.Packet;
import eu.themetacloudservice.networking.worker.Options;
import eu.themetacloudservice.networking.worker.Worker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

public class ServerSession implements Structure {


    private final Worker worker;


    public ServerSession() {
        this.worker = new Worker();
    }


    public Worker getWorker() {
        return worker;
    }

    public void bind(int port) throws IOException {
        worker.bind(new InetSocketAddress(port));
    }

    @Override
    public void close() {
        worker.close();
    }

    @Override
    public void sendPacket(Packet packet) {
        worker.sendPacket(packet);
    }

    @Override
    public ServerSession init(IChannelInitializer initializer) {
        worker.init(initializer);
        return this;
    }

    @Override
    public ServerSession option(Options<?> option, Object value) {
        worker.option(option, value);
        return this;
    }

    @Override
    public <T> T getOption(Options<T> option) {
        return worker.getOption(option);
    }

    @Override
    public boolean isConnected() {
        return worker.isConnected();
    }

    public InetAddress getInetAddress() {
        return worker.getInetAddress();
    }

    public SocketAddress getLocalSocketAddress() {
        return worker.getLocalSocketAddress();
    }

    public int getPort() {
        return worker.getPort();
    }

    public List<IChannel> getChannels() {
        return worker.getChannels();
    }
}
