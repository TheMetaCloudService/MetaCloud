package eu.metacloudservice.cloudplayer.offlineplayer.ceched;


import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
public class OfflinePlayerCache implements IConfigAdapter {

    private String username;
    private String uniqueID;
    private String firstConnected;
    private String lastConnected;
    private String lastProxy;
    private String lastService;


    public OfflinePlayerCache(){}
}
