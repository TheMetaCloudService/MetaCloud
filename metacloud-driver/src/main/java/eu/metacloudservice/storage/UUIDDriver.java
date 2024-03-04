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

public class UUIDDriver {

    private static ArrayList<UUIDStorage> uuids;
    public static String getUUID(String name) {
        if (uuids == null){
            uuids = new ArrayList<>();
        }
        if (uuids.stream().anyMatch(uuidStorage -> uuidStorage.getUsername().equalsIgnoreCase(name))){
            return uuids.stream().filter(uuidStorage -> uuidStorage.getUsername().equalsIgnoreCase(name)).findFirst().get().getUniqueID();
        }else {
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
                String trimmedUUIDString = json.getString("uuid");
                StringBuilder fullUUIDBuilder = new StringBuilder(trimmedUUIDString);
                fullUUIDBuilder.insert(20, "-");
                fullUUIDBuilder.insert(16, "-");
                fullUUIDBuilder.insert(12, "-");
                fullUUIDBuilder.insert(8, "-");
                String uuid = fullUUIDBuilder.toString();
                uuids.add(new UUIDStorage(name, uuid));

                reader.close();

                return uuid;
            } catch (Exception e) {
                try {
                    URL url = new URL("https://api.minetools.eu/uuid/" + name);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    JSONObject json = new JSONObject(builder.toString());
                    String trimmedUUIDString = json.getString("id");
                    StringBuilder fullUUIDBuilder = new StringBuilder(trimmedUUIDString);
                    fullUUIDBuilder.insert(20, "-");
                    fullUUIDBuilder.insert(16, "-");
                    fullUUIDBuilder.insert(12, "-");
                    fullUUIDBuilder.insert(8, "-");
                    String uuid = fullUUIDBuilder.toString();
                    reader.close();
                    uuids.add(new UUIDStorage(name, uuid));
                    return uuid;
                }catch (Exception ignored){
                    try {
                        URL url = new URL("https://api.minetools.eu/uuid/" + name);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                        JSONObject json = new JSONObject(builder.toString());
                        String trimmedUUIDString = json.getString("id");
                        StringBuilder fullUUIDBuilder = new StringBuilder(trimmedUUIDString);
                        fullUUIDBuilder.insert(20, "-");
                        fullUUIDBuilder.insert(16, "-");
                        fullUUIDBuilder.insert(12, "-");
                        fullUUIDBuilder.insert(8, "-");
                        String uuid = fullUUIDBuilder.toString();
                        reader.close();
                        uuids.add(new UUIDStorage(name, uuid));
                        return uuid;
                    }catch (Exception E){
                        try {
                            URL url = new URL("https://api.minecraftservices.com/minecraft/profile/lookup/name/" + name);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                            StringBuilder builder = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                builder.append(line);
                            }
                            JSONObject json = new JSONObject(builder.toString());
                            String trimmedUUIDString = json.getString("id");
                            StringBuilder fullUUIDBuilder = new StringBuilder(trimmedUUIDString);
                            fullUUIDBuilder.insert(20, "-");
                            fullUUIDBuilder.insert(16, "-");
                            fullUUIDBuilder.insert(12, "-");
                            fullUUIDBuilder.insert(8, "-");
                            String uuid = fullUUIDBuilder.toString();
                            reader.close();
                            uuids.add(new UUIDStorage(name, uuid));
                            return uuid;
                        }catch (Exception exception){}
                    }
                }
            }
        }
        return null;

    }


    public static String getUsername(String uuid) {

        if (uuids == null){
            uuids = new ArrayList<>();
        }

        if (uuids.stream().anyMatch(uuidStorage -> uuidStorage.getUniqueID().equalsIgnoreCase(uuid))){
            return uuids.stream().filter(uuidStorage -> uuidStorage.getUniqueID().equalsIgnoreCase(uuid)).findFirst().get().getUsername();
        }else {
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
                String username = json.getString("pseudo");
                uuids.add(new UUIDStorage(username, uuid));

                reader.close();
                return username;
            } catch (Exception e) {
                try {
                    URL url = new URL("https://api.minetools.eu/uuid/" + uuid);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    JSONObject json = new JSONObject(builder.toString());
                    String username = json.getString("name");
                    uuids.add(new UUIDStorage(username, uuid));

                    reader.close();
                    return username;
                }catch (Exception ignored){}
            }
        }
        return null;

    }


}
