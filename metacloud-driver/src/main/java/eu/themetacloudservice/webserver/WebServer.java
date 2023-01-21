package eu.themetacloudservice.webserver;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.webserver.entry.RouteEntry;
import lombok.SneakyThrows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WebServer {


    private ArrayList<RouteEntry> routes;
    private ServerSocket webServer;
    private boolean interrupted;

    public WebServer() {
        interrupted = false;
        this.routes = new ArrayList<>();
        this.HandelConnections();
    }

    public RouteEntry getRoute(String route){

            for (RouteEntry entry : routes){
                if (entry.GET_ROUTE().equalsIgnoreCase(route)){
                    return entry;
                }
            }

        return null;
    }

    public WebServer addRoute(RouteEntry entry){
        routes.add(entry);
        return this;
    }

    public void removeRoute(String route){
        this.routes.removeIf(entry -> entry.GET_ROUTE().equals(route));
    }


    @SneakyThrows
    private void HandelConnections(){

        int port = 8090;
        AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
        String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
        if (new File("./service.json").exists()){
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            port = config.getRestApiCommunication();
        }else {
            NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
            port = config.getRestApiCommunication();
        }
        
        this.webServer = new ServerSocket(port);
        this.webServer.setPerformancePreferences(0, 2, 10);

            new Thread(() -> {
                    while (!interrupted){
                        Socket connection = null;
                        try {
                            connection = this.webServer.accept();
                        } catch (IOException e) {

                        }
                        try {

                            connection.setPerformancePreferences(0, 2, 10);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String[] tokens = reader.readLine().split(" ");

                            String method = tokens[0];
                            String rawroute = tokens[1];

                            if (rawroute.contains("/")){

                             try {
                                 String key = rawroute.split("/")[1];
                                 String route = rawroute.replace("/"+key, "");

                                 if (!key.equals(authCheckKey)){
                                     sendResponse(connection, "error 404", "application/json", "{\"reason\":\"please enter your auth-key\"}");

                                 }else {
                                     if (method.equals("GET")) {
                                         if (getRoute(route) != null) {
                                             sendResponse(connection, "200 OK", "application/json", getRoute(route).GET());
                                         } else {
                                             sendResponse(connection, "error 404", "application/json", "{\"reason\":\"no data found\"}");
                                         }
                                     } else if (method.equals("PUT")) {
                                         if (getRoute(route) != null) {
                                             handlePut(route, connection, reader);
                                         } else {
                                             sendResponse(connection, "error 404", "application/json", "{\"reason\":\"no data found\"}");
                                         }
                                     } else {
                                         sendResponse(connection, "error 404", "application/json", "{\"reason\":\"please enter GET ore PUT\"}");
                                     }
                                 }
                             }catch (Exception e){
                                 sendResponse(connection, "error 404", "application/json", "{\"reason\":\"please enter your auth-key\"}");
                             }
                            }else {
                                sendResponse(connection, "error 404", "application/json", "{\"reason\":\"please enter your auth-key\"}");

                            }
                        } catch (IOException e) {
                        }finally {
                            try {
                                connection.close();
                            } catch (IOException e) {
                            }
                        }
                }
            }).start();

    }

    private void handlePut(String path, Socket clientSocket, BufferedReader in) throws IOException {
        // Beispiel für die Verarbeitung einer PUT-Anfrage
        String headerLine;
        int contentLength = 0;
        while ((headerLine = in.readLine()).length() != 0) {
            if (headerLine.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(headerLine.split(" ")[1]);
            }
        }
        char[] body = new char[contentLength];
        in.read(body, 0, contentLength);
        String data = new String(body);
        getRoute(path).PUT(data);
        sendResponse(clientSocket, "200 OK", "application/json", "{\"reason\":\"data received\"}");
    }

    @SneakyThrows
    private void sendResponse(Socket connection, String status, String contentType, String response){
        PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
        out.println("HTTP/1.1 " + status);
        out.println("Content-Type: " + contentType);
        out.println("Content-Length: " + response.length());
        out.println();
        out.println(response);
        out.flush();
        out.close();
    }
}
