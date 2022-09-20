package eu.themetacloudservice.networking;


import eu.themetacloudservice.networking.channels.Channel;
import eu.themetacloudservice.networking.channels.IChannel;
import eu.themetacloudservice.networking.channels.IChannelInitializer;
import eu.themetacloudservice.networking.protocol.Packet;
import eu.themetacloudservice.networking.worker.Options;
import java.net.InetSocketAddress;

public class ClientSession implements  Structure{

    private final Channel channel = new Channel(this);

    private String host;
    private Integer port;

    public ClientSession() {
    }



    public ClientSession bind(String host, int port) {
        this.host = host;
        this.port = port;
        return this;
    }

    public void connect() {

            channel.connect(new InetSocketAddress(host,port));
            channel.start();


    }

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public void sendPacket(Packet packet) {
        channel.sendPacket(packet);
    }


    @Override
    public ClientSession option(Options<?> option, Object value) {
        option.setValue(value);
        return this;
    }

    @Override
    public <T> T getOption(Options<T> option) {
        return option.value;
    }

    @Override
    public ClientSession init(IChannelInitializer initializer) {
        channel.init(initializer);
        return this;
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    public IChannel getChannel() {
        return channel;
    }
}
