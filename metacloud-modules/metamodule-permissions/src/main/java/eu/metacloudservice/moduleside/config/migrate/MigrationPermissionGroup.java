/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.moduleside.config.migrate;


import eu.metacloudservice.moduleside.config.IncludedAble;
import eu.metacloudservice.moduleside.config.PermissionAble;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class MigrationPermissionGroup {

    private String group;
    private Boolean isDefault;
    private int tagPower;
    private String prefix;
    private String suffix;
    private ArrayList<PermissionAble> permissions;
    private ArrayList<IncludedAble> included;


    public MigrationPermissionGroup(){}

}
