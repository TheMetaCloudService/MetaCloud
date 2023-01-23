package eu.themetacloudservice.webserver;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestDriver {


    private String ip;
    private int port;

    public RestDriver(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @SneakyThrows
    public String put(String route, String content){
        ConfigDriver configDriver = new ConfigDriver("./connection.key");
        AuthenticatorKey authConfig = (AuthenticatorKey) configDriver.read(AuthenticatorKey.class);
        String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
        URL url = new URL("http://" + ip+":" + port + "/" +authCheckKey + route);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(content.getBytes());
        os.flush();
        os.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer c = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            c.append(inputLine);
        }
        in.close();

        return c.toString();
    }

    @SneakyThrows
    public String get(String route){
        AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
        String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
        URL url = new URL("http://" + ip+":" + port + "/" +authCheckKey+ route);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(false);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }
}
