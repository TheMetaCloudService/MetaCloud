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
        final ObjectMapper objectMapper = new ObjectMapper();
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


    public String update(String route, IRest content){

        String result = null;
        try {
           final ConfigDriver configDriver = new ConfigDriver("./connection.key");
            final AuthenticatorKey authConfig = (AuthenticatorKey) configDriver.read(AuthenticatorKey.class);
            final String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
            final String urlString = String.format("http://%s:%d/%s%s", ip, port, authCheckKey, route);
            final URL url = new URL(urlString);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("PUT");
            con.setDoOutput(true);
            con.setConnectTimeout(5000); // Set connection timeout to 5 seconds
            con.setReadTimeout(5000);    // Set read timeout to 5 seconds
            con.connect();

            try (final OutputStream os = con.getOutputStream()) {
                os.write(GSON.toJson(content).getBytes());
                os.flush();
            }

            final int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (final InputStream inputStream = con.getInputStream();
                     final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                     final BufferedReader in = new BufferedReader(inputStreamReader)) {

                    final StringBuilder response = new StringBuilder();
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


    public String create(String route, IRest content){

        String result = null;
        try {
            final ConfigDriver configDriver = new ConfigDriver("./connection.key");
            final AuthenticatorKey authConfig = (AuthenticatorKey) configDriver.read(AuthenticatorKey.class);
            final String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
            final String urlString = String.format("http://%s:%d/%s%s", ip, port, authCheckKey, route);
            final URL url = new URL(urlString);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(5000); // Set connection timeout to 5 seconds
            con.setReadTimeout(5000);    // Set read timeout to 5 seconds
            con.connect();

            try (final OutputStream os = con.getOutputStream()) {
                os.write(GSON.toJson(content).getBytes());
                os.flush();
            }

            final int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (final InputStream inputStream = con.getInputStream();
                     final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                     final BufferedReader in = new BufferedReader(inputStreamReader)) {

                    final StringBuilder response = new StringBuilder();
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

    public String create(String route, String content){

        String result = null;
        try {
            final ConfigDriver configDriver = new ConfigDriver("./connection.key");
            final AuthenticatorKey authConfig = (AuthenticatorKey) configDriver.read(AuthenticatorKey.class);
            final String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
            final String urlString = String.format("http://%s:%d/%s%s", ip, port, authCheckKey, route);
            final URL url = new URL(urlString);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(5000); // Set connection timeout to 5 seconds
            con.setReadTimeout(5000);    // Set read timeout to 5 seconds
            con.connect();

            try (final OutputStream os = con.getOutputStream()) {
                os.write(content.getBytes());
                os.flush();
            }

            final int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (final InputStream inputStream = con.getInputStream();
                     final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                     final BufferedReader in = new BufferedReader(inputStreamReader)) {

                    final StringBuilder response = new StringBuilder();
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
    public String update(String route, String content){

        String result = null;
        try {
            final ConfigDriver configDriver = new ConfigDriver("./connection.key");
            final AuthenticatorKey authConfig = (AuthenticatorKey) configDriver.read(AuthenticatorKey.class);
            final String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
            final String urlString = String.format("http://%s:%d/%s%s", ip, port, authCheckKey, route);
            final URL url = new URL(urlString);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("PUT");
            con.setDoOutput(true);
            con.setConnectTimeout(5000); // Set connection timeout to 5 seconds
            con.setReadTimeout(5000);    // Set read timeout to 5 seconds
            con.connect();

            try (final OutputStream os = con.getOutputStream()) {
                os.write(content.getBytes());
                os.flush();
            }

            final int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (final InputStream inputStream = con.getInputStream();
                     final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                     final BufferedReader in = new BufferedReader(inputStreamReader)) {

                    final StringBuilder response = new StringBuilder();
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


    public IRest get(String route, Class<? extends IRest> tClass){
        String content = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;
        BufferedReader in = null;

        try {
            final AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
            final String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
            final URL url = new URL("http://" + ip + ":" + port + "/" + authCheckKey + route);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setDoOutput(false);
            con.setConnectTimeout(5000); // Set connection timeout to 5 seconds
            con.setReadTimeout(5000);    // Set read timeout to 5 seconds
            con.connect();

            final int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                inputStream = con.getInputStream();
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                in = new BufferedReader(inputStreamReader);

                final StringBuilder response = new StringBuilder();
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
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(content, tClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String get(String route){
        String content = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;
        BufferedReader in = null;

        try {
            final  AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
            final String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
            final URL url = new URL("http://" + ip + ":" + port + "/" + authCheckKey + route);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setDoOutput(false);
            con.setConnectTimeout(5000); // Set connection timeout to 5 seconds
            con.setReadTimeout(5000);    // Set read timeout to 5 seconds
            con.connect();

            final int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                inputStream = con.getInputStream();
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                in = new BufferedReader(inputStreamReader);

                final StringBuilder response = new StringBuilder();
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

    public String delete(String route){
        String content = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;
        BufferedReader in = null;

        try {
            final  AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
            final  String authCheckKey = Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey());
            final  URL url = new URL("http://" + ip + ":" + port + "/" + authCheckKey + route);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("DELETE");
            con.setDoOutput(false);
            con.setConnectTimeout(5000); // Set connection timeout to 5 seconds
            con.setReadTimeout(5000);    // Set read timeout to 5 seconds
            con.connect();

            final int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                inputStream = con.getInputStream();
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                in = new BufferedReader(inputStreamReader);

                final StringBuilder response = new StringBuilder();
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


    public String getWithoutAuth(String urls){
        String content = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;
        BufferedReader in = null;

        try {
            final URL url = new URL(urls);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setDoOutput(false);
            con.setConnectTimeout(5000); // Set connection timeout to 5 seconds
            con.setReadTimeout(5000);    // Set read timeout to 5 seconds
            con.connect();

            final int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                inputStream = con.getInputStream();
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                in = new BufferedReader(inputStreamReader);

                final  StringBuilder response = new StringBuilder();
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
