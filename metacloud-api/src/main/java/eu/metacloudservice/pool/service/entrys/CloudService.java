package eu.metacloudservice.pool.service.entrys;

import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.process.ServiceState;
import lombok.NonNull;

import java.util.ArrayList;

public class CloudService {

    private String name;
    private String group;

    public CloudService(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public void setState(@NonNull ServiceState state){

    }

    public Group getGroup(){
    return null;
    }

    public ServiceState getState(){
        return null;
    }


    public ArrayList<CloudPlayer> getPlayers(){
        return null;
    }

}
