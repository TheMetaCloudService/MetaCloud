package eu.metacloudservice.cloudplayer.offlineplayer.migrate;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class MigrateOfflinePlayer implements IConfigAdapter {

    private String username;
    private UUID uniqueId;
    private String firstConnected;
    private String lastConnected;
    private String lastProxy;
    private String lastService;

    public MigrateOfflinePlayer() {
    }
}
