/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver.remastered.handel;

import eu.metacloudservice.Driver;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class RequestPUT extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            if (request.method().name().equals("PUT")) {
                String uri = request.uri();
                if (uri.contains("/")) {
                    String authenticatorKey = uri.split("/")[1];
                    if (Driver.getInstance().wb.AUTH_KEY.contains(authenticatorKey)){
                        String path = uri.replace("/" + authenticatorKey, "");
                        ByteBuf content = request.content();
                        String payload = content.toString(CharsetUtil.UTF_8);
                    Driver.getInstance().wb.updateRoute(path, payload);
                        FullHttpResponse response = createResponse(HttpResponseStatus.OK, "{\"reason\":\"data received\"}");
                        ctx.writeAndFlush(response);
                    }else {
                        FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
                        ctx.writeAndFlush(response);
                    }
                } else {
                    FullHttpResponse response = createResponse(HttpResponseStatus.NOT_FOUND, "{\"reason\":\"please enter the right auth-key\"}");
                    ctx.writeAndFlush(response);
                }
            }
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
