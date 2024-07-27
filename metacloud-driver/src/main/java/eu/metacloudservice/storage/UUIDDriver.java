package eu.metacloudservice.storage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class UUIDDriver {
    private static ArrayList<UUIDStorage> uuids;
    public static UUID getUUID(@NonNull final String name) {
        if (uuids == null){
            uuids = new ArrayList<>();
        }
        if (uuids.stream().anyMatch(uuidStorage -> uuidStorage.getUsername().equalsIgnoreCase(name))){
            return uuids.stream().filter(uuidStorage -> uuidStorage.getUsername().equalsIgnoreCase(name)).findFirst().get().getUniqueID();
        }else {
            try {
                final URL url = new URL("https://playerdb.co/api/player/minecraft/" + name);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                final StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                final JSONObject json = new JSONObject(builder.toString());

                final UUID uuid = UUID.fromString(json.getJSONObject("data").getJSONObject("player").getString("id"));
                reader.close();
                uuids.add(new UUIDStorage(name, uuid));
                return uuid;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;

    }


    public static String getUsername(@NonNull final UUID uuid) {

        if (uuids == null){
            uuids = new ArrayList<>();
        }

        if (uuids.stream().anyMatch(uuidStorage -> uuidStorage.getUniqueID().toString().equalsIgnoreCase(uuid.toString()))){
            return uuids.stream().filter(uuidStorage -> uuidStorage.getUniqueID().toString().equalsIgnoreCase(uuid.toString())).findFirst().get().getUsername();
        }else {

            try {
                final URL url = new URL("https://playerdb.co/api/player/minecraft/" + uuid.toString());
                final  BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                final StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                final JSONObject json = new JSONObject(builder.toString());

                final String name = json.getJSONObject("data").getJSONObject("player").getString("username");
                reader.close();
                uuids.add(new UUIDStorage(name, uuid));
                return name;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;

    }


}
