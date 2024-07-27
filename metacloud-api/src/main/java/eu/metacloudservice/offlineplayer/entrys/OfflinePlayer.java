package eu.metacloudservice.offlineplayer.entrys;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OfflinePlayer {

    private final String username;
    private final UUID uniqueId;
    private final String firstConnected;
    private final String lastConnected;
    private final String lastProxy;
    private final String lastService;
    private final int connectionCount;
    private final int serverSwitches;

}
