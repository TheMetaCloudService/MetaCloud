/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver.handel;

import eu.metacloudservice.Driver;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.NonNull;

public  class RequestGET  {

    public void handle(@NonNull final ChannelHandlerContext ctx, @NonNull final FullHttpRequest request) throws Exception {
        final String uri = request.getUri();
        if (uri.contains("/") && uri.length() > 7) {
            if (uri.contains("/setup/")){
                final String path = uri;
                if (Driver.getInstance().getWebServer().getRoutes(path) == null){
                    final FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter a right path\"}");
                    ctx.writeAndFlush(response);
                } else {
                    final String json = Driver.getInstance().getWebServer().getRoute(path);
                    final FullHttpResponse response = createResponse(HttpResponseStatus.OK, json);
                    Driver.getInstance().getWebServer().removeRoute(path);
                    ctx.writeAndFlush(response);
                }
            }else {
                final String authenticatorKey = uri.split("/")[1];
                if (authenticatorKey.length() > 4 && Driver.getInstance().getWebServer().AUTH_KEY.equals(authenticatorKey)){
                    final String path = uri.replace("/" + authenticatorKey, "");
                    if (Driver.getInstance().getWebServer().getRoutes(path) == null){
                        final  FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter a right path\"}");
                        ctx.writeAndFlush(response);
                    } else if (!path.isEmpty()){
                        final String json = Driver.getInstance().getWebServer().getRoute(path);
                        final  FullHttpResponse response = createResponse(HttpResponseStatus.OK, json);
                        ctx.writeAndFlush(response);
                    }else {
                        final FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter a right path\"}");
                        ctx.writeAndFlush(response);
                    }
                }else {
                    final FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
                    ctx.writeAndFlush(response);
                }
            }

        } else {
            final FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
            ctx.writeAndFlush(response);
        }
    }

    private FullHttpResponse createResponse(@NonNull final HttpResponseStatus status,@NonNull final String content) {
        final FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }
}
