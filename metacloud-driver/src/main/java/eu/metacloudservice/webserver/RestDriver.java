package eu.metacloudservice.webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.metacloudservice.webserver.interfaces.IRest;
import lombok.SneakyThrows;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RestDriver {

    private static final Gson GSON = (new GsonBuilder()).serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
    private String ip;
    private int port;

    public RestDriver(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public RestDriver() {
    }

    @SneakyThrows
    public IRest convert(String json, Class<? extends IRest> tClass){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String convert(IRest IRest){
        return GSON.toJson(IRest);
    }

    public String put(String route, String content){

        String result = null;
        try {
            ConfigDriver configDriver = new ConfigDriver("./connection.key");
            AuthenticatorKey authConfig = (AuthenticatorKey) configDriver.read(AuthenticatorKey.class);
            String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
            String urlString = String.format("http://%s:%d/%s%s", ip, port, authCheckKey, route);
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("PUT");
            con.setDoOutput(true);
            con.setConnectTimeout(5000); // Set connection timeout to 5 seconds
            con.setReadTimeout(5000);    // Set read timeout to 5 seconds
            con.connect();

            try (OutputStream os = con.getOutputStream()) {
                os.write(content.getBytes());
                os.flush();
            }

            int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = con.getInputStream();
                     InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                     BufferedReader in = new BufferedReader(inputStreamReader)) {

                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    result = response.toString();
                }
            } else {
                // Handle non-OK status code if needed
            }
        } catch (IOException e) {
            // Handle IOException if needed
        }
        return result;


    }
    public String get(String route){
        String content = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;
        BufferedReader in = null;

        try {
            AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
            String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
            URL url = new URL("http://" + ip + ":" + port + "/" + authCheckKey + route);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setDoOutput(false);
            con.setConnectTimeout(5000); // Set connection timeout to 5 seconds
            con.setReadTimeout(5000);    // Set read timeout to 5 seconds
            con.connect();

            int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                inputStream = con.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                in = new BufferedReader(inputStreamReader);

                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                content = response.toString();
            }
        } catch (IOException ignored) {
        } finally {
            // Ressourcenfreigabe im finally-Block
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // Handle exception
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Handle exception
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
        return content;
    }
}
