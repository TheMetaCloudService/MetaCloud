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
import eu.metacloudservice.webserver.handel.RequestGET;
import eu.metacloudservice.webserver.handel.RequestHandler;
import eu.metacloudservice.webserver.handel.RequestNotFound;
import eu.metacloudservice.webserver.handel.RequestPUT;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class WebServer {



    private ConcurrentLinkedDeque<RouteEntry> ROUTES;
    public  String AUTH_KEY;

    private  EventLoopGroup boosGroup;
    private  EventLoopGroup workerGroup;
    private  Thread current;

    @SneakyThrows
    public WebServer() {

        AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
        this.AUTH_KEY = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        this.ROUTES = new ConcurrentLinkedDeque<>();

         boosGroup = new NioEventLoopGroup(1);
         workerGroup = new NioEventLoopGroup();

            current = new Thread(() -> {
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap()
                            .group(boosGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer<>() {
                                @Override
                                protected void initChannel(Channel ch) {
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
                    ChannelFuture future = bootstrap.bind(config.getRestApiCommunication()).sync();

                    Channel channel = future.channel();
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
