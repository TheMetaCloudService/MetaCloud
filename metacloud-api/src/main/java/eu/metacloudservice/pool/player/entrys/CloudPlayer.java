package eu.metacloudservice.pool.player.entrys;

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
        return  null;
    }
    public CloudService getServer(){
        return  null;
    }

    public void connect(@NonNull CloudService cloudService){

    }

    public void kick(@NonNull String message){

    }

    public void sendMessage(@NonNull String message){

    }


}
