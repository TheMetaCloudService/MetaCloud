package eu.metacloudservice.networking.client;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.codec.PacketDecoder;
import eu.metacloudservice.networking.codec.PacketEncoder;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.unix.UnixChannelOption;

import java.net.InetSocketAddress;

public class NettyClient extends ChannelInitializer<Channel> implements AutoCloseable {
    private int port;
    private String host;
    private static final WriteBufferWaterMark WATER_MARK = new WriteBufferWaterMark(1 << 20, 1 << 21);
    private final boolean EPOLL = Epoll.isAvailable();
    private Channel channel;

    private EventLoopGroup  BOSS;

    public NettyClient bind(String host, int port) {
        this.port = port;
        this.host = host;
        return this;
    }

    public void connect() {
        boolean isEpoll = Epoll.isAvailable();

        BOSS =   isEpoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(BOSS)
                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, WATER_MARK)
                .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .handler(this);
        try {
            this.channel = bootstrap.connect(new InetSocketAddress(host, port)).sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        channel.close();
    }


    public Channel getChannel() {
        return channel;
    }

    @Override
    protected void initChannel(Channel channel) {

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

    }

    private boolean allowAddress(String address){
        if (NettyDriver.getInstance().getWhitelist().contains(address)){
            return true;
        }else {
            return false;
        }
    }

    public void sendPacketsSynchronized(final Packet... packets){
        for (Packet packet : packets)
            this.sendPacketSynchronized(packet);

    }

    public void sendPacketsAsynchronous(final Packet... packets){
        for (Packet packet : packets)
            this.sendPacketAsynchronous(packet);

    }

    public void sendPacketSynchronized(Packet packet){
        channel.writeAndFlush(packet);
    }

    public void sendPacketAsynchronous(Packet packet){
        new Thread(() -> {
            channel.writeAndFlush(packet);
            Thread.currentThread().stop();
        }).start();
    }
}
