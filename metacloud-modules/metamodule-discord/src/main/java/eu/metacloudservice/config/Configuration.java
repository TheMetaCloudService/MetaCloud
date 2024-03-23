package eu.metacloudservice.config;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
public class Configuration implements IConfigAdapter {
    @Setter
    @Getter
    private String discordToken;
    @Setter
    @Getter
    private String discordActivity;
    @Setter
    @Getter
    private Integer channelID;
    @Setter
    @Getter
    private String footer;
    @Setter
    @Getter
    private String logo;

}
