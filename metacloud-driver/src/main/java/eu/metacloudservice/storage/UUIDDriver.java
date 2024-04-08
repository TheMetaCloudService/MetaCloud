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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class UUIDDriver {


    private static final String ADD_UUID_HYPHENS_REGEX = "([a-f0-9]{8})([a-f0-9]{4})(4[a-f0-9]{3})([89aAbB][a-f0-9]{3})([a-f0-9]{12})";

    private static ArrayList<UUIDStorage> uuids;
    public static UUID getUUID(String name) {
        if (uuids == null){
            uuids = new ArrayList<>();
        }
        if (uuids.stream().anyMatch(uuidStorage -> uuidStorage.getUsername().equalsIgnoreCase(name))){
            return uuids.stream().filter(uuidStorage -> uuidStorage.getUsername().equalsIgnoreCase(name)).findFirst().get().getUniqueID();
        }else {
            try {
                URL url = new URL("https://playerdb.co/api/player/minecraft/" + name);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                JSONObject json = new JSONObject(builder.toString());

                UUID uuid = UUID.fromString(json.getJSONObject("data").getJSONObject("player").getString("id"));
                reader.close();
                uuids.add(new UUIDStorage(name, uuid));
                return uuid;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;

    }


    public static String getUsername(UUID uuid) {

        if (uuids == null){
            uuids = new ArrayList<>();
        }

        if (uuids.stream().anyMatch(uuidStorage -> uuidStorage.getUniqueID().toString().equalsIgnoreCase(uuid.toString()))){
            return uuids.stream().filter(uuidStorage -> uuidStorage.getUniqueID().toString().equalsIgnoreCase(uuid.toString())).findFirst().get().getUsername();
        }else {

            try {
                URL url = new URL("https://playerdb.co/api/player/minecraft/" + uuid.toString());
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                JSONObject json = new JSONObject(builder.toString());

                String name = json.getJSONObject("data").getJSONObject("player").getString("username");
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
