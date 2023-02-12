package eu.themetacloudservice.webserver;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.webserver.entry.RouteEntry;
import lombok.SneakyThrows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WebServer {


    private final ArrayList<RouteEntry> routes;
    private ServerSocket webServer;
    private Thread current;
    private final boolean interrupted;

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

    public void addRoute(RouteEntry entry){
        routes.add(entry);
    }

    public void updateRoute(String route, String json){
        this.routes.parallelStream().filter(routeEntry -> routeEntry.route.equalsIgnoreCase(route)).findFirst().get().UPDATE(json);
    }

    public void removeRoute(String route){
        this.routes.removeIf(entry -> entry.GET_ROUTE().equals(route));
    }


    @SneakyThrows
    private void HandelConnections(){
        AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
        String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        int port = config.getRestApiCommunication();
        this.webServer = new ServerSocket(port);
        current = new Thread(() -> {
            while (!interrupted) {
                Socket connection = null;
                try {
                    connection = webServer.accept();
                    Socket finalConnection = connection;
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(finalConnection.getInputStream(), StandardCharsets.UTF_8));
                        String[] tokens = reader.readLine().split(" ");
                        String method = tokens[0];
                        String rawroute = tokens[1];
                        if (rawroute.contains("/") && rawroute.split("/")[1].equals(authCheckKey) || rawroute.contains("/") && rawroute.split("/")[1].equals("debug")) {
                            String route = rawroute.replace("/" + rawroute.split("/")[1], "");
                            if (method.equals("GET")) {
                                sendResponse(finalConnection, "200 OK", getRoute(route).GET());
                            } else if (method.equals("PUT")) {
                                handlePut(route, finalConnection, reader);
                            } else {
                                sendResponse(finalConnection, "error 404", "{\"reason\":\"please enter GET ore PUT\"}");
                            }
                        } else {
                            sendResponse(finalConnection, "error 404", "{\"reason\":\"please enter your auth-key\"}");
                        }
                        finalConnection.close();

                    } catch (Exception e) {
                        sendResponse(finalConnection, "error 404", "{\"reason\":\"please enter your auth-key\"}");

                    }
                } catch (IOException e) {
                }

            }
        });
        current.setPriority(Thread.MIN_PRIORITY);
        current.start();
    }

    public void close(){
        current.stop();
    }

    private void handlePut(String path, Socket clientSocket, BufferedReader in) throws IOException {
        String headerLine;
        int contentLength = 0;
        while ((headerLine = in.readLine()).length() != 0) {
            if (headerLine.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(headerLine.trim().split(":")[1]);
            }
        }
        char[] body = new char[contentLength];
        in.read(body, 0, contentLength);
        String data = new String(body);
        getRoute(path).PUT(data);
        sendResponse(clientSocket, "200 OK", "{\"reason\":\"data received\"}");
    }

    @SneakyThrows
    private void sendResponse(Socket connection, String status, String response){
        PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
        out.println("HTTP/1.1 " + status);
        out.println("Content-Type: " + "application/json");
        out.println("Content-Length: " + response.length());
        out.println();
        out.println(response);
        out.flush();
        out.close();
    }
}
