/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver.handel;

import eu.metacloudservice.Driver;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPICreateEvent;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudRestAPICreateEvent;
import eu.metacloudservice.webserver.entry.RouteEntry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.NonNull;

public class RequestCREATE {

    public void handle(@NonNull final ChannelHandlerContext ctx, @NonNull final FullHttpRequest request) throws Exception {
        final  String uri = request.uri();
        if (uri.contains("/")) {
            if (uri.contains("/setup/")){
                return;
            }
            final  String authenticatorKey = uri.split("/")[1];
            if (authenticatorKey.length() > 4 && Driver.getInstance().getWebServer().AUTH_KEY.equals(authenticatorKey)){
                final  String path = uri.replace("/" + authenticatorKey, "");
                if (Driver.getInstance().getWebServer().getRoutes(path) != null){
                    final FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter a right path\"}");
                    ctx.writeAndFlush(response);
                } else if (!path.isEmpty()){
                    final ByteBuf content = request.content();
                    final String payload = content.toString(CharsetUtil.UTF_8);
                    Driver.getInstance().getWebServer().addRoute(new RouteEntry(path, payload));
                    Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudRestAPICreateEvent(path, payload));
                    NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutCloudRestAPICreateEvent(path, payload));
                    FullHttpResponse response = createResponse(HttpResponseStatus.OK, "{\"reason\":\"data received\"}");
                    ctx.writeAndFlush(response);
                }else {
                    final FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter a right path\"}");
                    ctx.writeAndFlush(response);
                }

            }else {
                final FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
                ctx.writeAndFlush(response);
            }
        } else {
            final FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
            ctx.writeAndFlush(response);
        }
    }

    private FullHttpResponse createResponse(@NonNull final HttpResponseStatus status, @NonNull final String content) {
        final FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }
}
