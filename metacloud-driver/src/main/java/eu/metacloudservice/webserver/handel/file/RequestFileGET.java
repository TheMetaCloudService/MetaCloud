/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver.handel.file;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class RequestFileGET {

    public void handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.getUri().replace("/STORAGE", "");
    }
}
