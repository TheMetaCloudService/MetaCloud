package eu.metacloudservice.webserver.handel;

import eu.metacloudservice.Driver;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class RequestDELETE {
    public void handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri();
        if (uri.contains("/")) {
            if (uri.contains("/setup/")){
                return;
            }
            String authenticatorKey = uri.split("/")[1];
            if (authenticatorKey.length() > 4 && (Driver.getInstance().getWebServer()).AUTH_KEY.equals(authenticatorKey)) {
                String path = uri.replace("/" + authenticatorKey, "");
                if (Driver.getInstance().getWebServer().getRoutes(path) == null) {
                    FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"the path not exists\"}");
                    ctx.writeAndFlush(response);
                } else if (!path.isEmpty()) {
                    Driver.getInstance().getWebServer().removeRoute(path);
                    FullHttpResponse response = createResponse(HttpResponseStatus.OK, "{\"reason\":\"data received\"}");
                    ctx.writeAndFlush(response);
                } else {
                    FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"the path your entered is empty\"}");
                    ctx.writeAndFlush(response);
                }
            } else {
                FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
                ctx.writeAndFlush(response);
            }
        } else {
            FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
            ctx.writeAndFlush(response);
        }
    }

    private FullHttpResponse createResponse(HttpResponseStatus status, String content) {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
        defaultFullHttpResponse.headers().set((CharSequence)HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        defaultFullHttpResponse.headers().set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(defaultFullHttpResponse.content().readableBytes()));
        return (FullHttpResponse)defaultFullHttpResponse;
    }
}
