/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.moduleside.config.migrate;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import eu.metacloudservice.moduleside.config.PermissionPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;


@Getter
@AllArgsConstructor
public class MigrationConfiguration implements IConfigAdapter {

    private ArrayList<MigrationPermissionGroup> groups;
    private ArrayList<PermissionPlayer> players;

    public MigrationConfiguration(){}

}
