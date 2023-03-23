package eu.metacloudservice.storage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class UUIDDriver {

    private static final String URL2 = "https://minecraft-api.com/api/pseudo/";
    public static String getUUID(String name) {
        String urlString = "https://minecraft-api.com/api/uuid/" + name + "/json";
        try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JSONObject json = new JSONObject(builder.toString());
            return json.getString("uuid");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


    public static String getUsername(String uuid) {
        String urlString = "https://minecraft-api.com/api/pseudo/" + uuid + "/json";
        try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JSONObject json = new JSONObject(builder.toString());
            return json.getString("pseudo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
