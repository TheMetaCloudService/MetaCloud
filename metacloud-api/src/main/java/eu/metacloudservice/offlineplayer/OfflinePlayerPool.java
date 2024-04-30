package eu.metacloudservice.offlineplayer;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.cloudplayer.offlineplayer.ceched.OfflinePlayerCacheConfiguration;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.offlineplayer.entrys.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OfflinePlayerPool{


    public OfflinePlayerPool() {}

    public ArrayList<OfflinePlayer> getAllOfflinePlayers(){
        ArrayList<OfflinePlayer> players = new ArrayList<>();

        OfflinePlayerCacheConfiguration configuration = (OfflinePlayerCacheConfiguration) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudplayer/offlinecache"), OfflinePlayerCacheConfiguration.class);
        configuration.getPlayerCaches().forEach(cache -> {
            players.add(new OfflinePlayer(cache.getUsername(), cache.getUniqueId(), cache.getFirstConnected(), cache.getLastConnected(),cache.getLastProxy(),cache.getLastService(), cache.getConnectionCount(), cache.getServerSwitches()));
        });
        return players;
    }

    public OfflinePlayer getOfflinePlayer(String name){
        return getAllOfflinePlayers().stream().filter(offlinePlayer -> offlinePlayer.getUsername().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public OfflinePlayer getOfflinePlayer(UUID uniqueID){
      return   getAllOfflinePlayers().stream().filter(offlinePlayer -> offlinePlayer.getUniqueId().equals(uniqueID.toString())).findFirst().orElse(null);
    }

    public List<OfflinePlayer> getOfflinePlayerFromProxy(String proxy){
        return getAllOfflinePlayers().stream().filter(offlinePlayer -> offlinePlayer.getLastProxy().equalsIgnoreCase(proxy)).toList();
    }

    public List<OfflinePlayer> getOfflinePlayerFromService(String service){
        return getAllOfflinePlayers().stream().filter(offlinePlayer -> offlinePlayer.getLastService().equalsIgnoreCase(service)).toList();
    }
}
