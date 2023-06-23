package eu.metacloudservice.async.pool.player.entrys;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.async.pool.service.entrys.CloudService;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.cloudplayer.codec.gamemode.GameMode;
import eu.metacloudservice.cloudplayer.codec.sounds.Sounds;
import eu.metacloudservice.cloudplayer.codec.teleport.Teleport;
import eu.metacloudservice.cloudplayer.codec.title.Title;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.networking.in.service.playerbased.apibased.*;
import eu.metacloudservice.process.ServiceState;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;

public class CloudPlayer {

    @lombok.Getter
    private final String username;
    @lombok.Getter
    private final String UniqueId;
    public CloudPlayer(@NonNull String username, @NonNull String uniqueId) {
        this.username = username;
        UniqueId = uniqueId;
    }

    public void performMore(Consumer<CloudPlayer> cloudPlayerConsumer) {
        cloudPlayerConsumer.accept(this);
    }

    public CloudService getProxyServer(){
        CloudPlayerRestCache cech = (CloudPlayerRestCache) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + getUniqueId()), CloudPlayerRestCache.class);
        return  AsyncCloudAPI.getInstance().getServicePool().getService(cech.getCloudplayerproxy());
    }
    public CloudService getServer(){
        CloudPlayerRestCache cech = (CloudPlayerRestCache) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + getUniqueId()), CloudPlayerRestCache.class);

        return  AsyncCloudAPI.getInstance().getServicePool().getService(cech.getCloudplayerservice());
    }

    public void connect(@NonNull CloudService cloudService){
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerConnect(username, cloudService.getName()));
    }

    public void dispatchCommand(@NonNull String command){
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketOutAPIPlayerDispactchCommand(username, command));
    }

    public void sendComponent(Component component){
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInCloudPlayerComponent(component, username));
    }

    public void connectRandom(String group){
        CloudService cloudService = AsyncCloudAPI.getInstance().getServicePool().getServicesByGroup(group).get(new Random().nextInt(AsyncCloudAPI.getInstance().getServicePool().getServicesByGroup(group).size()));
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerConnect(username, cloudService.getName()));
    }

    public void connect(CloudPlayer cloudPlayer){
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerConnect(username, cloudPlayer.getServer().getName()));
    }
    public void sendTabList(String header, String footer){
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerTab(username, header, footer));
    }
    public long getCurrentPlayTime(){
        CloudPlayerRestCache cech = (CloudPlayerRestCache) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + getUniqueId()), CloudPlayerRestCache.class);
        return  cech.getCloudplayerconnect();
    }

    public void disconnect(@NonNull String message){
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerKick(username, message));
    }

    public void disconnect(){
        disconnect("§cYour kicked form the Network");
    }

    public void playSound(@NonNull Sounds sound, int volume, int pitch) {
        getServer().dispatchCommand("playsound " + sound.toString().toUpperCase() + " " + this.username + " " + volume + " " + pitch);
    }

    public void teleport(Teleport teleport) {

        if (teleport.getPlayer() != null){
            getServer().dispatchCommand("tp " + this.username + " " + teleport.getPlayer());

        }else {
            getServer().dispatchCommand("tp " + this.username + " " + teleport.getPosX() + " " + teleport.getPosY() + " " + teleport.getPosZ());
        }

    }

    public void connectRanked(String group) {
        AsyncCloudAPI.getInstance()
                .getServicePool()
                .getServicesByGroup(group)
                .stream()
                .filter(cloudService -> (cloudService.getState() == ServiceState.LOBBY))
                .min(Comparator.comparingInt(CloudService::getPlayercount))
                .ifPresent(service -> AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerConnect(this.username, service.getName())));
    }

    public void changeGameMode(GameMode gameMode) {
        switch (gameMode) {
            case ADVENTURE -> getServer().dispatchCommand("gamemode ADVENTURE " + this.username);
            case CREATIVE -> getServer().dispatchCommand("gamemode CREATIVE " + this.username);
            case SURVIVAL -> getServer().dispatchCommand("gamemode SURVIVAL " + this.username);
            case SPECTATOR -> getServer().dispatchCommand("gamemode SPECTATOR " + this.username);
        }
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

    public void sendTitle(@NonNull Title title){
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerTitle(title.getTitle(), title.getSubtitle(), title.getFadeIn(), title.getStay(), title.getFadeOut(), username));
    }


    public void sendActionBar(String message){
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerActionBar(username, message));
    }

    public void sendMessage(@NonNull String message){
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerMessage(username, message));
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
