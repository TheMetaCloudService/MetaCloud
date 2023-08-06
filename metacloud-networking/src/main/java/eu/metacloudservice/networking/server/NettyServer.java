package eu.metacloudservice.networking.server;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.codec.PacketDecoder;
import eu.metacloudservice.networking.codec.PacketEncoder;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class NettyServer extends ChannelInitializer<Channel> implements AutoCloseable{
    private int port;
    private final boolean EPOLL = Epoll.isAvailable();
    private final HashMap<String, Channel> CHANNELS = new HashMap<>();
    EventLoopGroup WORKER;
    EventLoopGroup BOSS;
    public NettyServer bind(int port) {
        this.port = port;
        return this;
    }

    @SneakyThrows
    public void start() {

        boolean isEpoll = Epoll.isAvailable();

        int cores = Runtime.getRuntime().availableProcessors();
        int bossThreads = Math.max(1, cores / 2);
        int workerThreads = Math.max(1, cores / 2);
        this.BOSS = isEpoll ? new EpollEventLoopGroup(bossThreads) : new NioEventLoopGroup(bossThreads);
        this.WORKER = isEpoll ? new EpollEventLoopGroup(workerThreads) : new NioEventLoopGroup(workerThreads);


        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(BOSS, WORKER)
                .channel(isEpoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childHandler(this)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        if(isEpoll) {
            bootstrap
                    .childOption(EpollChannelOption.EPOLL_MODE, EpollMode.LEVEL_TRIGGERED)
                    .option(EpollChannelOption.TCP_FASTOPEN, 3)
                    .option(EpollChannelOption.SO_REUSEPORT, true);
        }

        bootstrap.bind(new InetSocketAddress(port)).sync().channel();
    }

    public void registerChannel(String receiver, Channel channel) {
        if (!this.CHANNELS.containsKey(receiver))
            this.CHANNELS.put(receiver, channel);
    }

    public boolean isChannelFound(String receiver){
        return this.CHANNELS.containsKey(receiver);
    }

    public void removeChannel(String receiver) {
        this.CHANNELS.get(receiver).close();
        this.CHANNELS.remove(receiver);
    }

    public void close() {
        this.CHANNELS.forEach((s, channel) -> {
            channel.close();
        });
        WORKER.shutdownGracefully();
        BOSS.shutdownGracefully();
    }

    @Override
    protected void initChannel(Channel channel) {


        final InetSocketAddress inetSocketAddress = ((InetSocketAddress) channel.remoteAddress());

        if (allowAddress(inetSocketAddress.getAddress().getHostAddress())){
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(new ChannelHandler() {
                @Override
                public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {}
                @Override
                public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {}
                @Override
                public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {}});
            pipeline.addLast(new PacketDecoder());
            pipeline.addLast(new PacketEncoder());
        }else {
            channel.close().addListener(ChannelFutureListener.CLOSE_ON_FAILURE).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
    }

    private boolean allowAddress(String address){
        if (NettyDriver.getInstance().whitelist.contains(address)){
            return true;
        }else {
            return false;
        }
    }

    public void sendToAllSynchronized(final Packet... packets){
        for (Packet packet : packets)
            this.sendToAllSynchronized(packet);

    }

    public void sendToAllAsynchronous(final Packet... packets){
        for (Packet packet : packets)
            this.sendToAllAsynchronous(packet);

    }


    public void sendToAllSynchronized(final Packet packet){
        this.CHANNELS.forEach((s, channel) -> channel.writeAndFlush(packet));
    }

    public void sendToAllAsynchronous(final Packet packet){
        new Thread(() -> {
            this.CHANNELS.forEach((s, channel) -> channel.writeAndFlush(packet));
            Thread.currentThread().stop();
        }).start();
    }
    public void sendPacketSynchronized(final String channel, final Packet packet){
        this.CHANNELS.get(channel).writeAndFlush(packet);
    }

    public void sendPacketAsynchronous(final String channel, final Packet packet){
        new Thread(() -> {
            this.CHANNELS.get(channel).writeAndFlush(packet);
            Thread.currentThread().stop();
        }).start();
    }

    public void sendPacketSynchronized(final String channel, final Packet... packets){
        for (Packet packet : packets)
            this.CHANNELS.get(channel).writeAndFlush(packet);
    }

    public void sendPacketAsynchronous(final String channel, final Packet... packets){
        new Thread(() -> {
            for (Packet packet : packets)
                this.CHANNELS.get(channel).writeAndFlush(packet);
            Thread.currentThread().stop();
        }).start();
    }

}
