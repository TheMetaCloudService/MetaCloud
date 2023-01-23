package eu.themetacloudservice.networking.client;

import eu.themetacloudservice.networking.codec.PacketDecoder;
import eu.themetacloudservice.networking.codec.PacketEncoder;
import eu.themetacloudservice.networking.packet.listeners.ChannelBound;
import eu.themetacloudservice.networking.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    private int port;
    private String host;
    private Channel channel;
    private Channel manager;

    public NettyClient bind(String host, int port) {
        this.port = port;
        this.host = host;
        return this;
    }

    public void connect() {
        (new Thread(() -> {
            EventLoopGroup eventLoopGroup = Epoll.isAvailable() ? (EventLoopGroup)new EpollEventLoopGroup() : (EventLoopGroup)new NioEventLoopGroup();
            try {
                this.channel = ((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group(eventLoopGroup)).option(ChannelOption.SO_SNDBUF, Integer.valueOf(2147483647))).option(ChannelOption.SO_RCVBUF, Integer.valueOf(2147483647))).channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)).handler((ChannelHandler)new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new PacketDecoder());
                        pipeline.addLast(new PacketEncoder());
                        pipeline.addLast(new ChannelBound());

                    }
                })).connect(this.host, this.port).sync().channel();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        })).start();
    }

    public void close(){
        channel.close();
    }

    public void setManager(Channel manager) {
        this.manager = manager;
    }

    public void sendPacket(Packet packet){
        manager.writeAndFlush(packet);
    }

}
