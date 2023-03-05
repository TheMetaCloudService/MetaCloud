package eu.metacloudservice.networking.client;

import eu.metacloudservice.networking.codec.PacketDecoder;
import eu.metacloudservice.networking.codec.PacketEncoder;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient{
    private int port;
    private String host;
    private final boolean EPOLL = Epoll.isAvailable();
    private Channel channel;

    private EventLoopGroup  BOSS;

    public NettyClient bind(String host, int port) {
        this.port = port;
        this.host = host;
        return this;
    }

    public boolean connect() {
        BOSS = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {
                Bootstrap bootstrap = new Bootstrap()
                    .group(BOSS)
                    .option(ChannelOption.AUTO_READ, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.IP_TOS, 24)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<>() {
                        protected void initChannel(Channel channel) {
                                try {
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
                                }catch (Exception Ignored){}
                        }
                    });
                ChannelFuture channelFuture = bootstrap.connect(this.host, this.port);
                channel = channelFuture.sync().channel();
                return true;
        } catch (InterruptedException exception) {
            return false;
        }
    }

    public void close(){
        channel.close();
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
