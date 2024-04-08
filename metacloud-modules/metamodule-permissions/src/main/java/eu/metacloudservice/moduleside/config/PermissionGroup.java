/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.moduleside.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.chrono.MinguoDate;
import java.util.ArrayDeque;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class PermissionGroup {

    private String group;
    private Boolean isDefault;
    private int tagPower;
    private String prefix;
    private String suffix;
    private String display;
    private String scoreboard;
    private ArrayList<PermissionAble> permissions;
    private ArrayList<IncludedAble> included;


    public PermissionGroup(){}

}
