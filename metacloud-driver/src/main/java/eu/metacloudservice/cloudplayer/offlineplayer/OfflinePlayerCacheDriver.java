package eu.metacloudservice.cloudplayer.offlineplayer;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.offlineplayer.ceched.OfflinePlayerCache;
import eu.metacloudservice.cloudplayer.offlineplayer.ceched.OfflinePlayerCacheConfiguration;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.util.ArrayList;

public class OfflinePlayerCacheDriver {

    public OfflinePlayerCacheDriver() {
        if (!new ConfigDriver("./local/storage/cloudPlayer.storage").exists())
            new ConfigDriver("./local/storage/cloudPlayer.storage").save(new OfflinePlayerCacheConfiguration(new ArrayList<>()));

        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/cloudplayer/offlinecache", new ConfigDriver().convert(readConfig())));
    }

    public OfflinePlayerCacheConfiguration readConfig(){
        return (OfflinePlayerCacheConfiguration) new ConfigDriver("./local/storage/cloudPlayer.storage").read(OfflinePlayerCacheConfiguration.class);
    }

    public void saveConfig(OfflinePlayerCacheConfiguration configuration) {
        new ConfigDriver("./local/storage/cloudPlayer.storage").save(configuration);
        Driver.getInstance().getWebServer().updateRoute("/cloudplayer/offlinecache", new ConfigDriver().convert(configuration));
    }
}
