package eu.metacloudservice.storage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UUIDDriver {

    private static final String URL = "https://api.minetools.eu/uuid/";

    @SneakyThrows
    public static String getUUID(String name) {
        URL url = new URL(URL + name);
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

        String json = content.toString();
        JsonObject jso = JsonParser.parseString(json).getAsJsonObject();

        return jso.get("id").getAsString();
    }



}
