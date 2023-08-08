/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver.remastered;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.webserver.entry.RouteEntry;
import eu.metacloudservice.webserver.remastered.handel.RequestGET;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;

import java.util.ArrayList;

public class WebServer {



    private final ArrayList<RouteEntry> ROUTES;
    public final String AUTH_KEY;
    private final ManagerConfig config;
    @SneakyThrows
    public WebServer() {

        AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
        this.AUTH_KEY = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
        this.config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        this.ROUTES = new ArrayList<>();

        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(new RequestGET()); // ALL GET REQUESTS WAS LOAD
                        }
                    });
            ChannelFuture future = bootstrap.bind(8080).sync();

            Channel channel = future.channel();
            channel.closeFuture().sync();

        }finally {
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }

    public String getRoute(String path){
        if (        ROUTES.parallelStream().noneMatch(routeEntry -> routeEntry.readROUTE().equalsIgnoreCase(path)))return null;

        return ROUTES.parallelStream().filter(routeEntry -> routeEntry.readROUTE().equalsIgnoreCase(path)).findFirst().get().channelRead();
    }

    public RouteEntry getRoutes(String path){

        return  ROUTES.parallelStream().filter(routeEntry -> routeEntry.readROUTE().equalsIgnoreCase(path)).findFirst().orElse(null);
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



}
