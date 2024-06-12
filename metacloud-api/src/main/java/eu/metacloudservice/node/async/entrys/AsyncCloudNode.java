package eu.metacloudservice.node.async.entrys;/*
 * this class is by RauchigesEtwas
 */


import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.player.entrys.CloudPlayer;
import eu.metacloudservice.service.entrys.CloudService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Getter
public class AsyncCloudNode {

    private String nodeName;
    private String address;



    public CompletableFuture<List<CloudService>> getService(){
        return CompletableFuture.supplyAsync( () ->CloudAPI.getInstance().getServicePool().getServices().stream().filter(cloudService -> cloudService.getGroup().getStorage().getRunningNode().equals(getNodeName())).toList());
    }

    public List<CloudPlayer> getPlayersServiceSide(){

        List<CloudPlayer> players = new ArrayList<>();
        CloudAPI.getInstance().getGroupPool().getGroups().stream().filter(group -> group.getStorage().getRunningNode().equals(getNodeName())).toList().forEach(group -> {
            players.addAll(CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(group.getGroup()));
        });
        return players;
    }

    public List<CloudPlayer> getPlayersProxySide(){
        List<CloudPlayer> players = new ArrayList<>();
        CloudAPI.getInstance().getGroupPool().getGroups().stream().filter(group -> group.getStorage().getRunningNode().equals(getNodeName())).toList().forEach(group -> {
            players.addAll(CloudAPI.getInstance().getPlayerPool().getPlayersByProxyGroup(group.getGroup()));
        });

        return players;
    }
}
