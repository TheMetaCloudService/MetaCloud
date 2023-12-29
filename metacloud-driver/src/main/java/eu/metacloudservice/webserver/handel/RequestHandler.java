/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver.handel;

import eu.metacloudservice.webserver.handel.file.RequestFileDELETE;
import eu.metacloudservice.webserver.handel.file.RequestFileGET;
import eu.metacloudservice.webserver.handel.file.RequestFilePUT;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

public class RequestHandler  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.getUri();
            if (request.method() == HttpMethod.GET){

                if (uri.contains("/STORAGE/")){
                    new RequestFileGET().handle(ctx, request);
                }else {
                    new RequestGET().handle(ctx, request);
                }
            }else if (request.method() == HttpMethod.PUT){
                if (uri.contains("/STORAGE/")){
                    new RequestFilePUT().handle(ctx, request);
                }else {
                    new RequestPUT().handle(ctx, request);
                }
            }else if (request.method() == HttpMethod.POST){
                new RequestPost().handle(ctx, request);
            }else if (request.method() == HttpMethod.DELETE){
                if (uri.contains("/STORAGE/")){
                    new RequestFileDELETE().handle(ctx, request);
                }else {
                    new RequestDELETE().handle(ctx, request);
                }
            }else {
                new RequestNotFound().handle(ctx);
            }
        }
    }
}
