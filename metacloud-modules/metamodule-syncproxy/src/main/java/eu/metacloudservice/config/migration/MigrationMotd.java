package eu.metacloudservice.config.migration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class MigrationMotd {

    private String protocol;
    private String firstline;
    private String secondline;
    private ArrayList<String> playerinfos;
}
