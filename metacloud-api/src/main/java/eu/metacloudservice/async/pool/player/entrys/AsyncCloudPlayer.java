package eu.metacloudservice.async.pool.player.entrys;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.async.pool.service.entrys.AsyncCloudService;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.cloudplayer.codec.gamemode.GameMode;
import eu.metacloudservice.cloudplayer.codec.sounds.Sounds;
import eu.metacloudservice.cloudplayer.codec.teleport.Teleport;
import eu.metacloudservice.cloudplayer.codec.title.Title;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.apibased.*;
import eu.metacloudservice.process.ServiceState;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;


public record AsyncCloudPlayer(String username, UUID uniqueId) {

    public AsyncCloudPlayer(@NonNull String username, @NonNull UUID uniqueId) {
        this.username = username;
        this.uniqueId = uniqueId;
    }

    public UUID getUniqueId(){
        return this.uniqueId;
    }
    public String getUsername(){
        return this.username;
    }
    public void performMore(Consumer<AsyncCloudPlayer> cloudPlayerConsumer) {
        cloudPlayerConsumer.accept(this);
    }

    public AsyncCloudService getProxyServer() {
        CloudPlayerRestCache cech = (CloudPlayerRestCache) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + uniqueId()), CloudPlayerRestCache.class);
        try {
            return CloudAPI.getInstance().getAsyncServicePool().getService(cech.getCloudplayerproxy()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public AsyncCloudService getServer() {
        CloudPlayerRestCache cech = (CloudPlayerRestCache) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + uniqueId()), CloudPlayerRestCache.class);

        try {
            return CloudAPI.getInstance().getAsyncServicePool().getService(cech.getCloudplayerservice()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect(@NonNull AsyncCloudService asyncCloudService) {
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerConnect(username, asyncCloudService.getName()));
    }

    public void dispatchCommand(@NonNull String command) {
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketOutAPIPlayerDispactchCommand(username, command));
    }

    public void sendComponent(Component component) {
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInCloudPlayerComponent(component, username));
    }

    public void connectRandom(String group) {
        AsyncCloudService asyncCloudService = null;
        try {
            asyncCloudService = (AsyncCloudService) CloudAPI.getInstance().getAsyncServicePool().getServicesByGroup(group).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerConnect(username, asyncCloudService.getName()));
    }

    public void connect(AsyncCloudPlayer asyncCloudPlayer) {
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerConnect(username, asyncCloudPlayer.getServer().getName()));
    }

    public void sendTabList(String header, String footer) {
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerTab(username, header, footer));
    }

    public long getCurrentPlayTime() {
        CloudPlayerRestCache cech = (CloudPlayerRestCache) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + uniqueId()), CloudPlayerRestCache.class);
        return cech.getCloudplayerconnect();
    }

    public void disconnect(@NonNull String message) {
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerKick(username, message));
    }

    public void disconnect() {
        disconnect("§cYour kicked form the Network");
    }

    public void playSound(@NonNull Sounds sound, int volume, int pitch) {
        getServer().dispatchCommand("playsound " + sound.toString().toUpperCase() + " " + this.username + " " + volume + " " + pitch);
    }

    public void teleport(Teleport teleport) {

        if (teleport.getPlayer() != null) {
            getServer().dispatchCommand("tp " + this.username + " " + teleport.getPlayer());

        } else {
            getServer().dispatchCommand("tp " + this.username + " " + teleport.getPosX() + " " + teleport.getPosY() + " " + teleport.getPosZ());
        }

    }

    public void connectRanked(String group) {
        try {
            CloudAPI.getInstance()
                    .getAsyncServicePool()
                    .getServicesByGroup(group).get()
                    .stream()
                    .filter(asyncCloudService -> (asyncCloudService.getState() == ServiceState.LOBBY))
                    .min(Comparator.comparingInt(AsyncCloudService::getPlayercount))
                    .ifPresent(service -> CloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerConnect(this.username, service.getName())));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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


    public void setXp(int amount) {
        getServer().dispatchCommand("xp " + username + " " + amount);
    }

    public void sendTitle(@NonNull Title title) {
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerTitle(title.getTitle(), title.getSubtitle(), title.getFadeIn(), title.getStay(), title.getFadeOut(), username));
    }

    public void sendActionBar(String message) {
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerActionBar(username, message));
    }

    public void sendMessage(@NonNull String message) {
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInAPIPlayerMessage(username, message));
    }

    public void sendMessage(@NonNull String... message) {
        for (String msg : message) {
            sendMessage(msg);
        }
    }

    public boolean isConnectedOnFallback() {
        return getServer().isTypeLobby();
    }

    public String toString() {
        return "username='" + username + "', uniqueId='" + uniqueId + "', proxy='" + getProxyServer().getName() + "', service='" + getServer().getName() + "', skinValue='" + getSkinValue() + "', skinSignature='" + getSkinSignature() + "', isConnectedOnFallback='" + isConnectedOnFallback() + "', currentPlayTime='" + getCurrentPlayTime() + "'";
    }

}
