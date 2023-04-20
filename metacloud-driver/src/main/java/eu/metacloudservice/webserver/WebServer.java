package eu.metacloudservice.webserver;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.webserver.entry.RouteEntry;
import eu.metacloudservice.webserver.interfaces.IWebServer;
import lombok.SneakyThrows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class WebServer implements IWebServer {


    private final ArrayList<RouteEntry> ROUTES;
    private ServerSocket WORKER;
    private Thread CURRENT;
    private final boolean INTERRUPTED;
    private final String AUTH_KEY;

    public WebServer() {
        INTERRUPTED = false;
        AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
        AUTH_KEY = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
        this.ROUTES = new ArrayList<>();
        try {
            this.HandelConnections();
        } catch (IOException ignored) {}
    }

    public String getRoute(String path){

        return  Objects.requireNonNull(ROUTES.parallelStream().filter(routeEntry -> routeEntry.readROUTE().equalsIgnoreCase(path)).findFirst().orElse(null)).channelRead();
    }

    public RouteEntry getRoutes(String path){

        return  ROUTES.parallelStream().filter(routeEntry -> routeEntry.readROUTE().equalsIgnoreCase(path)).findFirst().orElse(null);
    }
    public void addRoute(RouteEntry entry){
        ROUTES.add(entry);
    }

    public void updateRoute(String path, String json){
        this.ROUTES.parallelStream().filter(routeEntry -> routeEntry.route.equalsIgnoreCase(path)).findFirst().get().channelUpdate(json);
    }

    public void removeRoute(String path){
        this.ROUTES.removeIf(entry -> entry.route.equalsIgnoreCase(path));
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
        getRoutes(path).channelWrite(data);
        writeAndFlush(clientSocket, "200 OK", "{\"reason\":\"data received\"}");
    }



    @Override
    public void HandelConnections() throws IOException {
        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        int port = config.getRestApiCommunication();
        this.WORKER = new ServerSocket(port);
        CURRENT = new Thread(() -> {
            while (!this.INTERRUPTED) {
                Socket connection = null;
                try {
                    connection = WORKER.accept();
                    Socket finalConnection = connection;
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(finalConnection.getInputStream(), StandardCharsets.UTF_8));
                        String[] tokens = reader.readLine().split(" ");
                        String method = tokens[0];
                        String rawroute = tokens[1];
                        if (rawroute.contains("/")){
                            String key = rawroute.split("/")[1];
                            if (key.contains(AUTH_KEY)){
                                String query = rawroute.replace("/" + key, "");
                                if (method.equals("GET")){
                                    writeAndFlush(finalConnection, "200 OK", getRoutes(query).channelRead());
                                }else if (method.equals("PUT") && key.contains(AUTH_KEY)){
                                    handlePut(query, finalConnection, reader);
                                }else {
                                    writeAndFlush(finalConnection, "error 404", "{\"reason\":\"please enter GET ore PUT\"}");
                                }
                            }else {
                                writeAndFlush(finalConnection, "error 404", "{\"reason\":\"please enter the right auth-key\"}");
                            }
                        }else {
                            writeAndFlush(finalConnection, "error 404", "{\"reason\":\"the auth-key is forgotten to enter\"}");
                        }
                        finalConnection.close();

                    } catch (Exception e) {
                        finalConnection.close();
                    }
                } catch (IOException ignored) {}

            }
        });
        CURRENT.setPriority(Thread.MIN_PRIORITY);
        CURRENT.start();
    }

    @Override
    public void close() {
        CURRENT.stop();
    }

    @SneakyThrows
    @Override
    public void writeAndFlush(Socket connection, String status, String response) {
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
