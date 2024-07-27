package eu.metacloudservice.networking.server;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.codec.PacketDecoder;
import eu.metacloudservice.networking.codec.PacketEncoder;
import eu.metacloudservice.networking.codec.PacketLengthDeserializer;
import eu.metacloudservice.networking.codec.PacketLengthSerializer;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyServer extends ChannelInitializer<Channel> implements AutoCloseable{
    private int port;
    private final Map<String, Channel> CHANNELS = new ConcurrentHashMap<>();
   private EventLoopGroup WORKER;
    private EventLoopGroup BOSS;

    private ChannelFuture channelFuture;


    public NettyServer bind(final int port) {
        this.port = port;
        return this;
    }

    @SneakyThrows
    public void start() {

        final boolean isEpoll = Epoll.isAvailable();
        this.BOSS = isEpoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        this.WORKER = isEpoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        channelFuture = new ServerBootstrap()
                .group(BOSS, WORKER)
                .channel(isEpoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childHandler(this)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.AUTO_READ, true)
                .bind(new InetSocketAddress(port))
                .addListener(ChannelFutureListener.CLOSE_ON_FAILURE)
                .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
                .sync().channel().closeFuture();
    }

    public void registerChannel( @NonNull final String receiver, @NonNull final Channel channel) {
        if (!this.CHANNELS.containsKey(receiver))
            this.CHANNELS.put(receiver, channel);
    }

    public boolean isChannelFound( @NonNull final String receiver){
        return this.CHANNELS.containsKey(receiver);
    }

    public void removeChannel( @NonNull final String receiver) {
        if (this.CHANNELS.get(receiver) != null) {
            if (this.CHANNELS.get(receiver).isActive())
                this.CHANNELS.get(receiver).close();
            this.CHANNELS.remove(receiver);
        }

    }

    public void close() {
        this.CHANNELS.forEach((s, channel) -> {
            channel.close();
        });

        this.channelFuture.cancel(true);
        WORKER.shutdownGracefully();
        BOSS.shutdownGracefully();
    }

    @Override
    protected void initChannel(Channel channel) {
        final InetSocketAddress inetSocketAddress = ((InetSocketAddress) channel.remoteAddress());

        if (allowAddress(inetSocketAddress.getAddress().getHostAddress())){

            channel.pipeline()
                    .addLast("packet-length-deserializer", new PacketLengthDeserializer())
                    .addLast("packet-decoder", new PacketDecoder())
                    .addLast("packet-length-serializer", new PacketLengthSerializer())
                    .addLast("packet-encoder", new PacketEncoder())
                    .addLast("worker", new NettyServerWorker());
        }else {
            channel.close().addListener(ChannelFutureListener.CLOSE_ON_FAILURE).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
    }

    private boolean allowAddress(@NonNull final String address){
        return NettyDriver.getInstance().getWhitelist().contains(address);
    }

    public void sendToAllSynchronized(@NonNull final Packet... packets){
        for (final Packet packet : packets)
            this.sendToAllSynchronized(packet);

    }

    public void sendToAllAsynchronous(@NonNull final Packet... packets){
        for (Packet packet : packets)
            this.sendToAllAsynchronous(packet);

    }


    public void sendToAllSynchronized(@NonNull final Packet packet){
        this.CHANNELS.forEach((s, channel) -> channel.writeAndFlush(packet));
    }

    public void sendToAllAsynchronous(@NonNull final Packet packet){
         new Thread(() -> {
            this.CHANNELS.forEach((s, channel) -> channel.writeAndFlush(packet));
        }).start();
    }
    public void sendPacketSynchronized(@NonNull final String channel, @NonNull final Packet packet){
        this.CHANNELS.get(channel).writeAndFlush(packet);
    }

    public void sendPacketAsynchronous(@NonNull final String channel, @NonNull final Packet packet){
        new Thread(() -> {
            this.CHANNELS.get(channel).writeAndFlush(packet);

        }).start();
    }

    public void sendPacketSynchronized(@NonNull final String channel, @NonNull final Packet... packets){
        for (Packet packet : packets)
            this.CHANNELS.get(channel).writeAndFlush(packet);
    }

    public void sendPacketAsynchronous(@NonNull final String channel, @NonNull final Packet... packets){
        new Thread(() -> {
            for (Packet packet : packets)
                this.CHANNELS.get(channel).writeAndFlush(packet);

        }).start();
    }

}
