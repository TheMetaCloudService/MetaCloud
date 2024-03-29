package eu.metacloudservice.async.pool.node.entrys;/*
 * this class is by RauchigesEtwas
 */


import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.pool.service.entrys.CloudService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncCloudNode {

    private String nodeName;
    private String address;

    public AsyncCloudNode(String nodeName, String address) {
        this.nodeName = nodeName;
        this.address = address;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getAddress() {
        return address;
    }

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
