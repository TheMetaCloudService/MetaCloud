package eu.metacloudservice.cloudplayer.offlineplayer.ceched;


import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter@Setter
@AllArgsConstructor
public class OfflinePlayerCache implements IConfigAdapter {

    private String username;
    private UUID uniqueId;
    private String firstConnected;
    private String lastConnected;
    private String lastProxy;
    private String lastService;
    private int connectionCount;
    private int serverSwitches;


    public OfflinePlayerCache(){}
}
