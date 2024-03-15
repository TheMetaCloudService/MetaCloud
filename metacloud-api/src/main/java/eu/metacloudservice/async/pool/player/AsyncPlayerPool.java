package eu.metacloudservice.async.pool.player;

import eu.metacloudservice.async.pool.player.entrys.AsyncCloudPlayer;
import eu.metacloudservice.process.ServiceState;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AsyncPlayerPool {

    private final ArrayList<AsyncCloudPlayer> connectedPlayers;

    public AsyncPlayerPool() {
        this.connectedPlayers = new ArrayList<>();
    }

    public CompletableFuture<List<AsyncCloudPlayer>> getPlayers(){
        return CompletableFuture.supplyAsync( () ->connectedPlayers);
    }

    public CompletableFuture<AsyncCloudPlayer> getPlayer(@NonNull String username){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.username().equals(username)).findFirst().orElse(null));
    }

    public CompletableFuture<AsyncCloudPlayer> getPlayer(@NonNull UUID uniqueId){
        return CompletableFuture.supplyAsync( () -> connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.uniqueId().equals(uniqueId.toString().replace("-", ""))).findFirst().orElse(null));
    }

    public CompletableFuture<List<AsyncCloudPlayer>> getPlayersFromService(@NonNull String service){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getServer() == null? false :cloudPlayer.getServer().getName().equals(service)).collect(Collectors.toList()));
    }

    public CompletableFuture<List<AsyncCloudPlayer>> getPlayersFromServiceGroupByState(@NonNull String group, ServiceState state){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getServer() == null ? false : cloudPlayer.getServer().getGroup().getGroup().equals(group))
                .filter(cloudPlayer -> cloudPlayer.getServer().getState() == state)
                .collect(Collectors.toList()));
    }

    public CompletableFuture<List<AsyncCloudPlayer>> getPlayersFromProxyGroupByState(@NonNull String group, ServiceState state){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getProxyServer() == null ? false : cloudPlayer.getProxyServer().getGroup().getGroup().equals(group))
                .filter(cloudPlayer -> cloudPlayer.getProxyServer().getState() == state)
                .collect(Collectors.toList()));
    }

    public CompletableFuture<List<AsyncCloudPlayer>> getPlayersFromProxy(@NonNull String Proxy){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getProxyServer() == null ? false : cloudPlayer.getProxyServer().getName().equals(Proxy)).collect(Collectors.toList()));
    }

    public CompletableFuture<List<AsyncCloudPlayer>> getPlayersByProxyGroup(@NonNull String group){
        return CompletableFuture.supplyAsync(()->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getProxyServer() == null ? false : cloudPlayer.getProxyServer().getGroup().getGroup().equals(group)).collect(Collectors.toList()));
    }

    public CompletableFuture<List<AsyncCloudPlayer>> getPlayersByServiceGroup(@NonNull String group){
        return CompletableFuture.supplyAsync(() ->connectedPlayers.stream().filter(cloudPlayer -> cloudPlayer.getServer() == null ? false : cloudPlayer.getServer().getGroup().getGroup().equals(group)).collect(Collectors.toList()));
    }




    public boolean playerIsNotNull(@NonNull UUID uniqueId){
        return connectedPlayers.stream().anyMatch(cloudPlayer -> cloudPlayer.uniqueId().replace("-", "").equals(uniqueId.toString().replace("-", "")));
    }

    public boolean playerIsNotNull(@NonNull String username){
        return connectedPlayers.stream().anyMatch(cloudPlayer -> cloudPlayer.username().equals(username));
    }

    public void registerPlayer(@NonNull AsyncCloudPlayer asyncCloudPlayer){
        if (connectedPlayers.stream().noneMatch(cloudPlayer1 -> cloudPlayer1.uniqueId().replace("-", "").equals(asyncCloudPlayer.uniqueId().replace("-", "")))){
            connectedPlayers.add(asyncCloudPlayer);
        }
    }

    public void unregisterPlayer(@NonNull String username){
        connectedPlayers.removeIf(cloudPlayer -> cloudPlayer.username().equalsIgnoreCase(username));
    }
    public boolean unregisterPlayer(@NonNull UUID uniqueId){
        if (connectedPlayers.stream().anyMatch(cloudPlayer -> cloudPlayer.uniqueId().equals(uniqueId.toString().replace("-", "")))){
            connectedPlayers.removeIf(cloudPlayer -> cloudPlayer.uniqueId().equals(uniqueId.toString().replace("-", "")));
            return true;
        }else {
            return false;
        }
    }
}
