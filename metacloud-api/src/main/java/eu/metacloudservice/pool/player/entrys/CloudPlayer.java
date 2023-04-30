package eu.metacloudservice.pool.player.entrys;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.codec.GameMode;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.networking.in.service.playerbased.apibased.*;
import eu.metacloudservice.pool.service.entrys.CloudService;
import lombok.NonNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

public class CloudPlayer {

    private String username, UniqueId;

    public CloudPlayer(@NonNull String username,@NonNull String uniqueId) {
        this.username = username;
        UniqueId = uniqueId;
    }

    public String getUsername() {
        return username;
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public CloudService getProxyServer(){
        CloudPlayerRestCache cech = (CloudPlayerRestCache) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + getUniqueId()), CloudPlayerRestCache.class);
        return  CloudAPI.getInstance().getServicePool().getService(cech.getCloudplayerproxy());
    }

    public long getCurrentPlayTime(){
        CloudPlayerRestCache cech = (CloudPlayerRestCache) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + getUniqueId()), CloudPlayerRestCache.class);
        return  cech.getCloudplayerconnect();
    }
    public CloudService getServer(){
        CloudPlayerRestCache cech = (CloudPlayerRestCache) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + getUniqueId()), CloudPlayerRestCache.class);

        return  CloudAPI.getInstance().getServicePool().getService(cech.getCloudplayerservice());
    }

    public void connect(@NonNull CloudService cloudService){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerConnect(username, cloudService.getName()));
    }

    public void connectRandom(String group){
        CloudService cloudService = CloudAPI.getInstance().getServicePool().getServicesByGroup(group).get(new Random().nextInt(CloudAPI.getInstance().getServicePool().getServicesByGroup(group).size()));
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerConnect(username, cloudService.getName()));
    }

    public void dispatchCommand(@NonNull String command){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketOutAPIPlayerDispactchCommand(username, command));
    }
    public String getSkinValue() {
        String urlString = "https://minecraft-api.com/api/uuid/" + username + "/json";
        try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JSONObject json = new JSONObject(builder.toString());
            return json.getJSONObject("properties").getString("value");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getSkinSignature() {
        String urlString = "https://minecraft-api.com/api/uuid/" + username + "/json";
        try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JSONObject json = new JSONObject(builder.toString());
            return json.getJSONObject("properties").getString("signature");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void connect(CloudPlayer cloudPlayer){
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerConnect(username, cloudPlayer.getServer().getName()));
    }
    public void disconnect(@NonNull String message){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerKick(username, message));
    }

    public void disconnect(){
        disconnect("§cYour kicked form the Network");
    }

    public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerTitle(title, subTitle, fadeIn, stay, fadeOut, username));
    }

    public void sendTabList(String header, String footer){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerTab(username, header, footer));
    }
    public void sendActionBar(String message){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerActionBar(username, message));
    }

    public void sendMessage(@NonNull String message){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerMessage(username, message));
    }

    public void sendMessage(@NonNull String... message){
        for (String msg : message){
            sendMessage(msg);
        }
    }


    public boolean isConnectedOnFallback(){
        return getServer().isTypeLobby();
    }


}
