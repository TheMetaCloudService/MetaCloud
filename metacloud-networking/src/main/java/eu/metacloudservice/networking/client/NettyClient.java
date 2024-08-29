package eu.metacloudservice.networking.client;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.codec.PacketDecoder;
import eu.metacloudservice.networking.codec.PacketEncoder;
import eu.metacloudservice.networking.codec.PacketLengthDeserializer;
import eu.metacloudservice.networking.codec.PacketLengthSerializer;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.server.NettyServerWorker;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.NonNull;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient extends ChannelInitializer<Channel> implements AutoCloseable {
    private int port;
    private String host;
    private Channel channel;

    private EventLoopGroup  BOSS;

    public NettyClient bind(final String host, final int port) {
        this.port = port;
        this.host = host;
        return this;
    }

    public void connect() {
       final boolean isEpoll = Epoll.isAvailable();

        BOSS = isEpoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {
            this.channel = new Bootstrap()
                    .group(BOSS)
                    .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.AUTO_READ, true)
                    .handler(this)
                    .connect(new InetSocketAddress(host, port))
                    .syncUninterruptibly()
                    .sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void close(){
        channel.close();
        BOSS.shutdownGracefully();
    }


    public Channel getChannel() {
        return channel;
    }

    @Override
    protected void initChannel(Channel channel) {

        channel.pipeline()
                .addLast("packet-length-deserializer", new PacketLengthDeserializer())
                .addLast("packet-decoder", new PacketDecoder())
                .addLast("packet-length-serializer", new PacketLengthSerializer())
                .addLast("packet-encoder", new PacketEncoder())
                .addLast("worker", new NettyClientWorker());

    }

    private boolean allowAddress(@NonNull final String address){
        return NettyDriver.getInstance().getWhitelist().contains(address);
    }

    public void sendPacketsSynchronized(@NonNull final Packet... packets){
        for (final Packet packet : packets)
            this.sendPacketSynchronized(packet);

    }

    public void sendPacketsAsynchronous(@NonNull final Packet... packets){
        for (final Packet packet : packets)
            this.sendPacketAsynchronous(packet);

    }

    public void sendPacketSynchronized(@NonNull final Packet packet){
        channel.writeAndFlush(packet);
    }

    public void sendPacketAsynchronous(@NonNull final Packet packet){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            channel.writeAndFlush(packet);
            service.shutdown();
        });

    }
}
