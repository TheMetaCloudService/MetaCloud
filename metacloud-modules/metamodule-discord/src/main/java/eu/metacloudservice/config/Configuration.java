package eu.metacloudservice.config;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Configuration implements IConfigAdapter {

    private String discordToken;
    private String discordGuild;
    private Integer channelID;
    private String footer;
    private String logo;
    private ActivityConfiguration activity;


}
