package eu.themetacloudservice.networking.server;

import eu.themetacloudservice.networking.codec.PacketDecoder;
import eu.themetacloudservice.networking.codec.PacketEncoder;
import eu.themetacloudservice.networking.packet.ChannelBound;
import eu.themetacloudservice.networking.packet.Packet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class NettyServer {
    private int port;

    private Channel channel;

    private HashMap<String, Channel> channels = new HashMap<>();

    public NettyServer bind(int port) {
        this.port = port;
        return this;
    }

    public void start() {
        (new Thread(() -> {
            EventLoopGroup eventLoopGroup = Epoll.isAvailable() ? (EventLoopGroup)new EpollEventLoopGroup() : (EventLoopGroup)new NioEventLoopGroup();
            try {
                this.channel = ((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).group(eventLoopGroup).option(ChannelOption.SO_RCVBUF, Integer.valueOf(2147483647))).channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)).childHandler((ChannelHandler)new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new PacketDecoder());
                        pipeline.addLast(new PacketEncoder());
                        pipeline.addLast(new ChannelBound());
                    }
                }).bind(new InetSocketAddress("0.0.0.0", this.port)).sync().channel();
            } catch (InterruptedException interruptedException) {}
        })).start();
    }

    public void registerChannel(String receiver, Channel channel) {
        if (!this.channels.containsKey(receiver))
            this.channels.put(receiver, channel);
    }

    public void removeChannel(String receiver) {
        if (this.channels.containsKey(receiver))
            this.channels.remove(receiver);
    }

    public void close() {
        this.channel.close();
    }

    public void sendContainsPacket(String contains, Packet packet) {
        this.channels.forEach((s, channel) -> {
            if (s.contains(contains))
                channel.writeAndFlush(packet);
        });
    }

    public void sendToAllPackets(Packet packet) {
        this.channels.forEach((s, channel) -> channel.writeAndFlush(packet));
    }

    public void sendPacket(String receiver, Packet packet) {
        ((Channel)this.channels.get(receiver)).writeAndFlush(packet);
    }
}
