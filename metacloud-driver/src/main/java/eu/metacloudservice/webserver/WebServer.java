/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.webserver.entry.RouteEntry;
import eu.metacloudservice.webserver.handel.RequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.SneakyThrows;

import java.util.concurrent.ConcurrentLinkedDeque;

public class WebServer {

    private final ConcurrentLinkedDeque<RouteEntry> ROUTES;
    public final String AUTH_KEY;
    private final EventLoopGroup boosGroup;
    private final EventLoopGroup workerGroup;
    private final Thread current;

    @SneakyThrows
    public WebServer() {

        final AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
        this.AUTH_KEY = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
        final ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        this.ROUTES = new ConcurrentLinkedDeque<>();

         boosGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() :new NioEventLoopGroup();
         workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

            current = new Thread(() -> {
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap()
                            .group(boosGroup, workerGroup)
                            .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer<SocketChannel>() {

                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {

                                    ch.pipeline().addLast(new ChannelHandler() {
                                        @Override
                                        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                                        }
                                        @Override
                                        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                                        }
                                        @Override
                                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                        }
                                    });

                                    ch.pipeline().addLast(new HttpRequestDecoder());
                                    ch.pipeline().addLast(new HttpResponseEncoder());
                                    ch.pipeline().addLast(new HttpObjectAggregator(65536));
                                    ch.pipeline().addLast(new RequestHandler());
                                }
                            });
                    final ChannelFuture future = bootstrap.bind(config.getRestApiCommunication()).sync();

                    final Channel channel = future.channel();
                    channel.closeFuture().sync();
                }catch (Exception ignored){
                }
            });
        current.start();
    }

    public String getRoute(String path){
        if (ROUTES.parallelStream().noneMatch(routeEntry -> routeEntry.readROUTE().equalsIgnoreCase(path)))
            return null;
        else
            return ROUTES.parallelStream().filter(routeEntry -> routeEntry.readROUTE().equalsIgnoreCase(path)).findFirst().get().channelRead();
    }



    public RouteEntry getRoutes(String path){

        return  ROUTES.parallelStream().filter(routeEntry -> routeEntry.readROUTE().equalsIgnoreCase(path)).findFirst().orElse(null);
    }


    public boolean isContentExists(String path){
        return getRoute(path) != null;
    }


    public void addRoute(RouteEntry entry){
        ROUTES.add(entry);
    }

    public void updateRoute(String path, String json){
        this.ROUTES.parallelStream().filter(routeEntry -> routeEntry.route.equalsIgnoreCase(path)).findFirst().get().channelUpdate(json);
    }

    public void removeRoute(String path){
        this.ROUTES.removeIf(entry -> entry.route.equalsIgnoreCase(path));
    }

    public void close() {
        current.stop();
        current.interrupt();
        workerGroup.shutdownGracefully();
        boosGroup.shutdownGracefully();
    }
}
