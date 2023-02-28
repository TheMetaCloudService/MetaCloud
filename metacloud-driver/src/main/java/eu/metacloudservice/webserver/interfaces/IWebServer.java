package eu.metacloudservice.webserver.interfaces;

import java.io.IOException;
import java.net.Socket;

public interface IWebServer {

    void HandelConnections() throws IOException;
    void close();
    void writeAndFlush(Socket connection, String status, String response);

}
