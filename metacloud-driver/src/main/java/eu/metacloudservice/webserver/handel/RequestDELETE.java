package eu.metacloudservice.webserver.handel;

import eu.metacloudservice.Driver;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPICreateEvent;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIDeleteEvent;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudRestAPICreateEvent;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudRestAPIDeleteEvent;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import lombok.NonNull;

public class RequestDELETE {
    public void handle(@NonNull final ChannelHandlerContext ctx, @NonNull final FullHttpRequest request) throws Exception {
        final String uri = request.uri();
        if (uri.contains("/")) {
            if (uri.contains("/setup/")){
                return;
            }
            final String authenticatorKey = uri.split("/")[1];
            if (authenticatorKey.length() > 4 && (Driver.getInstance().getWebServer()).AUTH_KEY.equals(authenticatorKey)) {
                final String path = uri.replace("/" + authenticatorKey, "");
                if (Driver.getInstance().getWebServer().getRoutes(path) == null) {
                    final  FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"the path not exists\"}");
                    ctx.writeAndFlush(response);
                } else if (!path.isEmpty()) {
                    Driver.getInstance().getWebServer().removeRoute(path);
                    Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudRestAPIDeleteEvent(path));
                    NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutCloudRestAPIDeleteEvent(path));
                    final FullHttpResponse response = createResponse(HttpResponseStatus.OK, "{\"reason\":\"data received\"}");
                    ctx.writeAndFlush(response);
                } else {
                    final FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"the path your entered is empty\"}");
                    ctx.writeAndFlush(response);
                }
            } else {
                final FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
                ctx.writeAndFlush(response);
            }
        } else {
            final FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
            ctx.writeAndFlush(response);
        }
    }

    private FullHttpResponse createResponse(@NonNull final HttpResponseStatus status,@NonNull final String content) {
        final DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
        defaultFullHttpResponse.headers().set((CharSequence)HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        defaultFullHttpResponse.headers().set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(defaultFullHttpResponse.content().readableBytes()));
        return (FullHttpResponse)defaultFullHttpResponse;
    }
}
