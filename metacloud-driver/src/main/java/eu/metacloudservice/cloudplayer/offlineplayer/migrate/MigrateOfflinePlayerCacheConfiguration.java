package eu.metacloudservice.cloudplayer.offlineplayer.migrate;


import eu.metacloudservice.cloudplayer.offlineplayer.ceched.OfflinePlayerCache;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;


@Getter
@AllArgsConstructor
public class MigrateOfflinePlayerCacheConfiguration implements IConfigAdapter {

    private ArrayList<MigrateOfflinePlayer> playerCaches;

    public MigrateOfflinePlayerCacheConfiguration(){}

}
