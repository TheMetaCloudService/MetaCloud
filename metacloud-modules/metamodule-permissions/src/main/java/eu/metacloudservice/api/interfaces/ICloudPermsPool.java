package eu.metacloudservice.api.interfaces;

import eu.metacloudservice.api.dummy.Permission;
import eu.metacloudservice.config.groups.entry.GroupEntry;
import eu.metacloudservice.config.player.PlayerConfiguration;

import java.util.List;

public interface ICloudPermsPool {

    List<Permission> getPermissionOfGroup(String group);

    GroupEntry getGroup(String group);
    List<GroupEntry> getGivenGroupsOfGroup(String group);
    List<Permission> getPermissionOfPlayer(String Player);
    List<GroupEntry> getGroupsOfPlayer(String Player);
    PlayerConfiguration getPlayer(String player);
}
