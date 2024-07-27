/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver.handel;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.NonNull;

public  class RequestNotFound {

    public void handle(@NonNull final ChannelHandlerContext ctx) throws Exception {
        final FullHttpResponse response = createResponse();
        ctx.writeAndFlush(response);
    }

    private FullHttpResponse createResponse() {
       final FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.METHOD_NOT_ALLOWED,
                Unpooled.copiedBuffer("{\"reason\":\"Failed, because no HttpRequest was found\"}", CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        return response;
    }
}
