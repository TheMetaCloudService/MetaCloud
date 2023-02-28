package eu.metacloudservice.networking.server;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.codec.PacketDecoder;
import eu.metacloudservice.networking.codec.PacketEncoder;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class NettyServer extends ChannelInitializer<Channel> implements AutoCloseable{
    private int port;

    private final  String SPACE_STRING = " ", SLASH_STRING = "/", EMPTY_STRING ="";
    private final boolean EPOLL = Epoll.isAvailable();
    private final HashMap<String, Channel> CHANNELS = new HashMap<>();
    EventLoopGroup WORKER;
    EventLoopGroup BOSS;
    public NettyServer bind(int port) {
        this.port = port;
        return this;
    }

    public void start() {

        WORKER= EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        BOSS = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()

                    .group(WORKER, BOSS)
                    .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                    .option(ChannelOption.AUTO_READ, true)
                    .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childOption(ChannelOption.IP_TOS, 24)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.AUTO_READ, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)

                    .childHandler(this);

            ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress("0.0.0.0", this.port));
            channelFuture.sync().channel();
        } catch (InterruptedException ignore) {}
    }

    public void registerChannel(String receiver, Channel channel) {
        if (!this.CHANNELS.containsKey(receiver))
            this.CHANNELS.put(receiver, channel);
    }

    public boolean isChannelFound(String receiver){
        return this.CHANNELS.containsKey(receiver);
    }

    public void removeChannel(String receiver) {
        this.CHANNELS.remove(receiver);
    }

    public void close() {
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
