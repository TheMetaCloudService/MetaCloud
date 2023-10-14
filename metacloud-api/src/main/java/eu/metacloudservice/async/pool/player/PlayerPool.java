package eu.metacloudservice.async.pool.player;

import eu.metacloudservice.async.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.process.ServiceState;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PlayerPool {

    private final ArrayList<CloudPlayer> connectedPlayers;

    public PlayerPool() {
        this.connectedPlayers = new ArrayList<>();
    }

    public CompletableFuture<List<CloudPlayer>> getPlayers(){
        return CompletableFuture.supplyAsync( () ->connectedPlayers);
    }

    public CompletableFuture<CloudPlayer> getPlayer(@NonNull String username){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getUsername().equals(username)).findFirst().orElse(null));
    }

    public CompletableFuture<CloudPlayer> getPlayer(@NonNull UUID uniqueId){
        return CompletableFuture.supplyAsync( () -> connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getUniqueId().equals(uniqueId.toString())).findFirst().orElse(null));
    }

    public CompletableFuture<List<CloudPlayer>> getPlayersFromService(@NonNull String service){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getServer() == null? false :cloudPlayer.getServer().getName().equals(service)).collect(Collectors.toList()));
    }

    public CompletableFuture<List<CloudPlayer>> getPlayersFromServiceGroupByState(@NonNull String group, ServiceState state){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getServer() == null ? false : cloudPlayer.getServer().getGroup().getGroup().equals(group))
                .filter(cloudPlayer -> cloudPlayer.getServer().getState() == state)
                .collect(Collectors.toList()));
    }

    public CompletableFuture<List<CloudPlayer>> getPlayersFromProxyGroupByState(@NonNull String group, ServiceState state){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getProxyServer() == null ? false : cloudPlayer.getProxyServer().getGroup().getGroup().equals(group))
                .filter(cloudPlayer -> cloudPlayer.getProxyServer().getState() == state)
                .collect(Collectors.toList()));
    }

    public CompletableFuture<List<CloudPlayer>> getPlayersFromProxy(@NonNull String Proxy){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getProxyServer() == null ? false : cloudPlayer.getProxyServer().getName().equals(Proxy)).collect(Collectors.toList()));
    }

    public CompletableFuture<List<CloudPlayer>> getPlayersByProxyGroup(@NonNull String group){
        return CompletableFuture.supplyAsync(()->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getProxyServer() == null ? false : cloudPlayer.getProxyServer().getGroup().getGroup().equals(group)).collect(Collectors.toList()));
    }

    public CompletableFuture<List<CloudPlayer>> getPlayersByServiceGroup(@NonNull String group){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getServer() == null ? false : cloudPlayer.getServer().getGroup().getGroup().equals(group)).collect(Collectors.toList()));
    }




    public boolean playerIsNotNull(@NonNull UUID uniqueId){
        return connectedPlayers.stream().anyMatch(cloudPlayer -> cloudPlayer.getUniqueId().equals(uniqueId.toString()));
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
        if (connectedPlayers.stream().anyMatch(cloudPlayer -> cloudPlayer.getUniqueId().equals(uniqueId.toString()))){
            connectedPlayers.removeIf(cloudPlayer -> cloudPlayer.getUniqueId().equals(uniqueId.toString()));
            return true;
        }else {
            return false;
        }
    }
}
