package eu.metacloudservice.offlineplayer.entrys;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OfflinePlayer {

    private String username;
    private UUID uniqueId;
    private String firstConnected;
    private String lastConnected;
    private String lastProxy;
    private String lastService;
    private int connectionCount;
    private int serverSwitches;

}
