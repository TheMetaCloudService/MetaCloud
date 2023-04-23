package eu.metacloudservice.moduleside.drivers;

import eu.metacloudservice.config.groups.GroupConfiguration;
import eu.metacloudservice.config.groups.entry.GroupEntry;
import eu.metacloudservice.config.player.PlayerConfiguration;

import java.util.ArrayList;
import java.util.List;

public interface IPermissionDriver {

    void createGroup(GroupEntry groupEntry);
    void deleteGroup(String group);
    void updateGroup(GroupEntry groupEntry);

    boolean isDefaultGroup(String group);
    void checkGroups();

    void changeDefault(String gtoup);

    void addInclude(String group, String include, int days);

    boolean isInclude(String group, String include);
    void removeInclude(String group, String include);


    void setGroupPower(String group, int power);

    boolean hasGroupPermission(String group, String permission);

    void addPermissionToGroup(String group, String permission, boolean set);
    void removePermissionFromGroup(String group, String permission);
    GroupEntry getGroup(String name);
    List<GroupEntry> getGroups();

    List<GroupEntry> getDefaults();
    GroupConfiguration getGroupConfig();

    void updateRanks(String player);

    void addRankToPlayer(String player, String rank, int time);
    void createPlayer(String player);
    void updatePlayer(PlayerConfiguration configuration);
    PlayerConfiguration getPlayer(String name);
    List<PlayerConfiguration> getPlayers();


    void addPermissionToPlayer(String player, String permission, boolean set);
    void removePermissionToPlayer(String player, String permission);

    boolean playerHasGroup(String player, String group);
    boolean hasPermission(String player, String permission);


}
