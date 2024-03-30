package eu.metacloudservice.cloudplayer.offlineplayer.ceched;


import eu.metacloudservice.cloudplayer.offlineplayer.OfflinePlayerCacheDriver;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.*;

import java.util.ArrayList;


@Getter
@AllArgsConstructor
public class OfflinePlayerCacheConfiguration implements IConfigAdapter {

    private ArrayList<OfflinePlayerCache> playerCaches;

    public OfflinePlayerCacheConfiguration(){}

}
