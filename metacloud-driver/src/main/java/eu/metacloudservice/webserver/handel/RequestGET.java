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

public  class RequestGET  {

    public void handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.getUri();
        if (uri.contains("/") && uri.length() > 7) {
            if (uri.contains("/setup/")){
                String path = uri;
                if (Driver.getInstance().getWebServer().getRoutes(path) == null){
                    FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter a right path\"}");
                    ctx.writeAndFlush(response);
                } else {
                    String json = Driver.getInstance().getWebServer().getRoute(path);
                    FullHttpResponse response = createResponse(HttpResponseStatus.OK, json);
                    Driver.getInstance().getWebServer().removeRoute(path);
                    ctx.writeAndFlush(response);
                }
            }else {
                String authenticatorKey = uri.split("/")[1];
                if (authenticatorKey.length() > 4 && Driver.getInstance().getWebServer().AUTH_KEY.equals(authenticatorKey)){
                    String path = uri.replace("/" + authenticatorKey, "");
                    if (Driver.getInstance().getWebServer().getRoutes(path) == null){
                        FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter a right path\"}");
                        ctx.writeAndFlush(response);
                    } else if (!path.isEmpty()){
                        String json = Driver.getInstance().getWebServer().getRoute(path);
                        FullHttpResponse response = createResponse(HttpResponseStatus.OK, json);
                        ctx.writeAndFlush(response);
                    }else {
                        FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter a right path\"}");
                        ctx.writeAndFlush(response);
                    }
                }else {
                    FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
                    ctx.writeAndFlush(response);
                }
            }

        } else {
            FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
            ctx.writeAndFlush(response);
        }
    }

    private FullHttpResponse createResponse(HttpResponseStatus status, String content) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }
}
