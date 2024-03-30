/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver.handel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

import static io.netty.handler.codec.http.HttpMethod.*;

public class RequestHandler  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest) msg;
            HttpMethod method = request.method();

            if (method != null) {
                if (GET.equals(method)) {
                    new RequestGET().handle(ctx, request);
                } else if (PUT.equals(method)) {
                    new RequestUPDATE().handle(ctx, request);
                } else if (POST.equals(method)) {
                    new RequestCREATE().handle(ctx, request);
                } else if (DELETE.equals(method)) {
                    new RequestDELETE().handle(ctx, request);
                } else {
                    new RequestNotFound().handle(ctx);
                }
            } else {
                new RequestNotFound().handle(ctx);
            }
        }
    }
}
