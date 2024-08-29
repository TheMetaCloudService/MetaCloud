package eu.metacloudservice.cloudplayer.offlineplayer;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.offlineplayer.ceched.OfflinePlayerCache;
import eu.metacloudservice.cloudplayer.offlineplayer.ceched.OfflinePlayerCacheConfiguration;
import eu.metacloudservice.cloudplayer.offlineplayer.migrate.MigrateOfflinePlayerCacheConfiguration;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.File;
import java.util.ArrayList;

public class OfflinePlayerCacheDriver {

    private ConfigDriver configDriver;

    public OfflinePlayerCacheDriver() {

        this.configDriver = new ConfigDriver("./local/storage/cloudPlayer.storage");

        if (!this.configDriver.exists())
            this.configDriver.save(new OfflinePlayerCacheConfiguration(new ArrayList<>()));
        else if (!this.configDriver.canBeRead(OfflinePlayerCacheConfiguration.class)){
            MigrateOfflinePlayerCacheConfiguration migration = (MigrateOfflinePlayerCacheConfiguration) this.configDriver.read(MigrateOfflinePlayerCacheConfiguration.class);
            ArrayList<OfflinePlayerCache> caches = new ArrayList<>();
            migration.getPlayerCaches().forEach(migrateOfflinePlayer -> caches.add(new OfflinePlayerCache(migrateOfflinePlayer.getUsername(),migrateOfflinePlayer.getUniqueId(), migrateOfflinePlayer.getFirstConnected(), migrateOfflinePlayer.getLastConnected(), migrateOfflinePlayer.getLastProxy(), migrateOfflinePlayer.getLastService(), 0, 0)));

           OfflinePlayerCacheConfiguration configuration = new OfflinePlayerCacheConfiguration(caches);

            new File("./local/storage/cloudPlayer.storage").deleteOnExit();
            this.configDriver.save(configuration);
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/cloudplayer/offlinecache", this.configDriver.convert(configuration)));

            return;

        }

        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/cloudplayer/offlinecache", this.configDriver.convert(readConfig())));
    }

    public OfflinePlayerCacheConfiguration readConfig(){
        return (OfflinePlayerCacheConfiguration) new ConfigDriver("./local/storage/cloudPlayer.storage").read(OfflinePlayerCacheConfiguration.class);
    }

    public void saveConfig(OfflinePlayerCacheConfiguration configuration) {
        this.configDriver.save(configuration);
        Driver.getInstance().getWebServer().updateRoute("/cloudplayer/offlinecache", this.configDriver.convert(configuration));
    }
}
