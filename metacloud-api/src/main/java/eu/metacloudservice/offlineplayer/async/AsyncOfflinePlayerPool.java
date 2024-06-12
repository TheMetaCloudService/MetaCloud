package eu.metacloudservice.offlineplayer.async;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.offlineplayer.async.entrys.AsyncOfflinePlayer;
import eu.metacloudservice.cloudplayer.offlineplayer.ceched.OfflinePlayerCacheConfiguration;
import eu.metacloudservice.configuration.ConfigDriver;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@NoArgsConstructor
public class AsyncOfflinePlayerPool{

    public CompletableFuture<ArrayList<AsyncOfflinePlayer>> getAllAsyncOfflinePlayers(){
        CompletableFuture.supplyAsync(()-> {
        ArrayList<AsyncOfflinePlayer> players = new ArrayList<>();

        OfflinePlayerCacheConfiguration configuration = (OfflinePlayerCacheConfiguration) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/offlinecache"), OfflinePlayerCacheConfiguration.class);
        configuration.getPlayerCaches().forEach(cache -> {
            players.add(new AsyncOfflinePlayer(cache.getUsername(), cache.getUniqueId(), cache.getFirstConnected(), cache.getLastConnected(),cache.getLastProxy(),cache.getLastService(), cache.getConnectionCount(), cache.getServerSwitches()));
        });
        return players;
        });
        return null;
    }

    public CompletableFuture<AsyncOfflinePlayer> getAsyncOfflinePlayer(String name){
        return CompletableFuture.supplyAsync(()-> {
            try {
                return getAllAsyncOfflinePlayers().get().parallelStream().filter(AsyncOfflinePlayer -> AsyncOfflinePlayer.getUsername().equalsIgnoreCase(name)).findFirst().orElse(null);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<AsyncOfflinePlayer> getAsyncOfflinePlayer(UUID uniqueID){
      return   CompletableFuture.supplyAsync(()-> {
          try {
              return getAllAsyncOfflinePlayers().get().parallelStream().filter(AsyncOfflinePlayer -> AsyncOfflinePlayer.getUniqueId().equals(uniqueID)).findFirst().orElse(null);
          } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
          }
      });
    }

    public CompletableFuture<List<AsyncOfflinePlayer>> getAsyncOfflinePlayerFromProxy(String proxy){
        return CompletableFuture.supplyAsync(()-> {
            try {
                return getAllAsyncOfflinePlayers().get().parallelStream().filter(AsyncOfflinePlayer -> AsyncOfflinePlayer.getLastProxy().equalsIgnoreCase(proxy)).toList();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<List<AsyncOfflinePlayer>> getAsyncOfflinePlayerFromService(String service){
        return CompletableFuture.supplyAsync(()-> {
            try {
                return getAllAsyncOfflinePlayers().get().parallelStream().filter(AsyncOfflinePlayer -> AsyncOfflinePlayer.getLastService().equalsIgnoreCase(service)).toList();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
