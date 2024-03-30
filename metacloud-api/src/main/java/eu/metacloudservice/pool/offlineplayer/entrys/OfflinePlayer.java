package eu.metacloudservice.pool.offlineplayer.entrys;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfflinePlayer {

    private String username;
    private String uniqueID;
    private String firstConnected;
    private String lastConnected;
    private String lastProxy;
    private String lastService;

}
