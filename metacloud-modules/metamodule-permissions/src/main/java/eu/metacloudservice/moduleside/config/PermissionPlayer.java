/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.moduleside.config;

import eu.metacloudservice.storage.UUIDDriver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
@AllArgsConstructor
public class PermissionPlayer {

    private String uuid;
    private ArrayList<IncludedAble> groups = new ArrayList<>();
    private ArrayList<PermissionAble> permissions = new ArrayList<>();

    public PermissionPlayer(){}


}
