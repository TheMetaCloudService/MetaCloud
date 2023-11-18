/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.webserver;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

public class PasteDriver {
    @SneakyThrows
    public String paste(String text){
        URL url = new URL("https://paste.metacloudservice.eu/documents");
        URLConnection connection = url.openConnection();

        connection.setRequestProperty("authority", "hastebin.com");
        connection.setRequestProperty("accept", "application/json, text/javascript, /; q=0.01");
        connection.setRequestProperty("x-requested-with", "XMLHttpRequest");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36'");
        connection.setRequestProperty("content-type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);

        OutputStream stream = connection.getOutputStream();
        stream.write(text.getBytes());
        stream.flush();
        stream.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.lines().collect(Collectors.joining("\n"));

        return "https://paste.metacloudservice.eu/" + response.split("\"")[3];
    }
}
