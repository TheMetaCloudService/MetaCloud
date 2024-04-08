package eu.metacloudservice.pool.player;

import eu.metacloudservice.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.process.ServiceState;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerPool {


    private final ArrayList<CloudPlayer> connectedPlayers;

    public PlayerPool() {
        this.connectedPlayers = new ArrayList<>();
    }

    public List<CloudPlayer> getPlayers(){
        return connectedPlayers;
    }

    public CloudPlayer getPlayer(@NonNull String username){
        return connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getUsername().equals(username)).findFirst().orElse(null);
    }

    public CloudPlayer getPlayer(@NonNull UUID uniqueId){
        return connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getUniqueId().equals(uniqueId.toString().replace("-", ""))).findFirst().orElse(null);
    }

    public List<CloudPlayer> getPlayersFromService(@NonNull String service){
        return connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getServer().getName().equals(service)).collect(Collectors.toList());
    }

    public List<CloudPlayer> getPlayersFromProxy(@NonNull String Proxy){
        return connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getProxyServer() == null ? false : cloudPlayer.getProxyServer().getName().equals(Proxy)).collect(Collectors.toList());
    }

    public List<CloudPlayer> getPlayersByProxyGroup(@NonNull String group){
        return connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getProxyServer() == null ? false : cloudPlayer.getProxyServer().getGroup().getGroup().equals(group)).collect(Collectors.toList());
    }

    public List<CloudPlayer> getPlayersByServiceGroup(@NonNull String group){
        return connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getServer() == null ? false : cloudPlayer.getServer().getGroup().getGroup().equals(group)).collect(Collectors.toList());
    }

    public List<CloudPlayer> getPlayersFromServiceGroupByState(@NonNull String group, ServiceState state){
        return connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getServer() == null? false : cloudPlayer.getServer().getGroup().getGroup().equals(group))
                .filter(cloudPlayer -> cloudPlayer.getServer().getState() == state)
                .collect(Collectors.toList());
    }

    public List<CloudPlayer> getPlayersFromProxyGroupByState(@NonNull String group, ServiceState state){
        return connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getProxyServer() == null ? false : cloudPlayer.getProxyServer().getGroup().getGroup().equals(group))
                .filter(cloudPlayer -> cloudPlayer.getProxyServer().getState() == state)
                .collect(Collectors.toList());
    }

    public boolean playerIsNotNull(@NonNull UUID uniqueId){
        return connectedPlayers.stream().anyMatch(cloudPlayer -> cloudPlayer.getUniqueId().equals(uniqueId.toString().replace("-", "")));
    }

    public boolean playerIsNotNull(@NonNull String username){
        return connectedPlayers.stream().anyMatch(cloudPlayer -> cloudPlayer.getUsername().equals(username));
    }

    public void registerPlayer(@NonNull CloudPlayer cloudPlayer){
        if (connectedPlayers.stream().noneMatch(cloudPlayer1 -> cloudPlayer1.getUniqueId().equals(cloudPlayer.getUniqueId()))){
            connectedPlayers.add(cloudPlayer);
        }
    }

    public void unregisterPlayer(@NonNull String username){
        connectedPlayers.removeIf(cloudPlayer -> cloudPlayer.getUsername().equalsIgnoreCase(username));
    }

    public boolean unregisterPlayer(@NonNull UUID uniqueId){
        if (connectedPlayers.stream().anyMatch(cloudPlayer -> cloudPlayer.getUniqueId().equals(uniqueId.toString().replace("-", "")))){
            connectedPlayers.removeIf(cloudPlayer -> cloudPlayer.getUniqueId().equals(uniqueId.toString().replace("-", "")));
            return true;
        }else {
            return false;
        }
    }
}
