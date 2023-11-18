/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver.handel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

public class RequestHandler  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest) msg;
            if (request.method() == HttpMethod.GET){
                new RequestGET().handle(ctx, request);
            }else if (request.method() == HttpMethod.PUT){
                new RequestPUT().handle(ctx, request);
            }else if (request.method() == HttpMethod.POST){
                new RequestPost().handle(ctx, request);
            }else if (request.method() == HttpMethod.DELETE){
                new RequestDELETE().handle(ctx, request);
            }else {
                new RequestNotFound().handle(ctx);
            }
        }
    }
}
