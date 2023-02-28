package eu.metacloudservice.pool.player.entrys;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCech;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketInAPIPlayerConnect;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketInAPIPlayerKick;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketInAPIPlayerMessage;
import eu.metacloudservice.pool.player.components.CloudComponent;
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

        CloudPlayerRestCech cech = (CloudPlayerRestCech) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + getUniqueId()), CloudPlayerRestCech.class);
        return  CloudAPI.getInstance().getServicePool().getService(cech.getCurrentProxy());
    }
    public CloudService getServer(){
        CloudPlayerRestCech cech = (CloudPlayerRestCech) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/" + getUniqueId()), CloudPlayerRestCech.class);

        return  CloudAPI.getInstance().getServicePool().getService(cech.getCurrentService());
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



    public void sendMessage(@NonNull String... message){
        for (String msg : message){
            sendMessage(msg);
        }
    }

}
