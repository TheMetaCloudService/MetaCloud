/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.pool.node.entrys;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.pool.service.entrys.CloudService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CloudNode {

    private String nodeName;
    private String address;

    public CloudNode(String nodeName, String address) {
        this.nodeName = nodeName;
        this.address = address;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getAddress() {
        return address;
    }

    public List<CloudService> getService(){
        return CloudAPI.getInstance().getServicePool().getServices().stream().filter(cloudService -> cloudService.getGroup().getStorage().getRunningNode().equals(getNodeName())).toList();
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
