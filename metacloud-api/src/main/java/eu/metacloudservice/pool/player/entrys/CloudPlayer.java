package eu.metacloudservice.pool.player.entrys;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.networking.in.service.playerbased.apibased.*;
import eu.metacloudservice.pool.service.entrys.CloudService;
import lombok.NonNull;

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
    public CloudService getServer(){
        CloudPlayerRestCache cech = (CloudPlayerRestCache) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + getUniqueId()), CloudPlayerRestCache.class);

        return  CloudAPI.getInstance().getServicePool().getService(cech.getCloudplayerservice());
    }

    public void connect(@NonNull CloudService cloudService){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerConnect(username, cloudService.getName()));
    }

    public void kick(@NonNull String message){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerKick(username, message));
    }

    public void sendMessage(@NonNull String message){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerMessage(username, message));
    }

    public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerTitle(title, subTitle, fadeIn, stay, fadeOut, username));
    }

    public void sendActionBar(String message){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInAPIPlayerActionBar(username, message));
    }

    public void sendMessage(@NonNull String... message){
        for (String msg : message){
            sendMessage(msg);
        }
    }

}
