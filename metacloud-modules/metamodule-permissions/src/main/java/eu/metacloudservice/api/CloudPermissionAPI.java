/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.api;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.moduleside.MetaModule;
import eu.metacloudservice.moduleside.config.*;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;

public class CloudPermissionAPI {
    private static CloudPermissionAPI instance;
    private Configuration configuration;

    public CloudPermissionAPI() {
        instance = this;
        if (MetaModule.instance == null){
            configuration = (Configuration) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/module/permission/configuration"), Configuration.class);
            new TimerBase().scheduleAsync(new TimerTask() {
                @Override
                public void run() {
                    configuration = (Configuration) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/module/permission/configuration"), Configuration.class);
                }
            }, 5, 5, TimeUtil.SECONDS);
        }
    }

    public static CloudPermissionAPI getInstance() {
        return instance;
    }

    public ArrayList<PermissionPlayer> getPlayers(){
        return getConfig() == null ? new ArrayList<>() : getConfig().getPlayers();
    }

    public void updateConfig(@NotNull Configuration configuration){
        if (MetaModule.instance == null) {
            this.configuration = configuration;
            CloudAPI.getInstance().getRestDriver().update("/module/permission/configuration", new ConfigDriver().convert(configuration));
        }else
            Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
    }

    public Configuration getConfig(){
        if (MetaModule.instance == null)
           return this.configuration;
        else
            return (Configuration) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/module/permission/configuration"), Configuration.class);
    }

    public ArrayList<PermissionPlayer> getPlayersByGroup(String group){
        return (ArrayList<PermissionPlayer>) getConfig().getPlayers().stream().filter(player -> player.getGroups().stream().anyMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(group))).toList();
    }

    public PermissionPlayer getPlayer(@NotNull String username){
        return getPlayers().stream().filter(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(username))).findFirst().orElse(null);
    }

    public PermissionPlayer getPlayerByUUID(@NotNull String uuid){
        return getPlayers().stream().filter(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(uuid)).findFirst().orElse(null);
    }

    public ArrayList<PermissionAble> getPermissionsFromPlayer(@NotNull String username){
        ArrayList<PermissionAble> permissionAbles = new ArrayList<>();
        getPlayer(username).getGroups().forEach(includedAble -> permissionAbles.addAll(getPermissionsFormGroup(includedAble.getGroup())));
        permissionAbles.addAll(getPlayer(username).getPermissions());
        return permissionAbles;
    }

    public ArrayList<PermissionAble> getPermissionsFormPlayerByUUID(@NotNull String uuid){
        ArrayList<PermissionAble> permissionAbles = new ArrayList<>();
        getPlayerByUUID(uuid).getGroups().forEach(includedAble -> permissionAbles.addAll(getPermissionsFormGroup(includedAble.getGroup())));
        permissionAbles.addAll(getPlayerByUUID(uuid).getPermissions());
        return permissionAbles;
    }

    public void setGroupToPlayer(String  uuid, String group, String time){
       PermissionPlayer player = getPlayerByUUID(uuid);
        if (isGroupExists(group)){
            player.getGroups().clear();
            player.getGroups().add(new IncludedAble(group, time));
            updatePlayer(player);
        }
    }

    public boolean updatePlayer(@NotNull PermissionPlayer player){
        Configuration configuration = getConfig();
        if (configuration == null)return false;
        else if (configuration.getPlayers().stream().noneMatch(player1 -> player1.getUuid().equalsIgnoreCase(player.getUuid()))) return false;
        else {
            configuration.getPlayers().removeIf(player1 -> player1.getUuid().equalsIgnoreCase(player.getUuid()));
            configuration.getPlayers().add(player);
            updateConfig(configuration);
            return true;
        }
    }

    public boolean addPermissionToPlayer(@NotNull String player, @NotNull PermissionAble permission){
        PermissionPlayer pp = getPlayer(player);
        if (pp == null) return false;
        else if (pp.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission.getPermission()))) return false;
        else {
            pp.getPermissions().add(permission);
            updatePlayer(pp);
            return true;
        }
    }

    public boolean removePermissionFromPlayer(@NotNull String player, @NotNull String permission){
        PermissionPlayer pp = getPlayer(player);
        if (pp == null) return false;
        else if (pp.getPermissions().stream().noneMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))) return false;
        else {
            pp.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission));
            updatePlayer(pp);
            return true;
        }

    }

    public boolean addGroupToPlayer(@NotNull String player, @NotNull IncludedAble group){
        PermissionPlayer pp = getPlayer(player);
        if (pp == null) return false;
        else if (pp.getGroups().stream().anyMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(group.getGroup()))) return false;
        else {
            pp.getGroups().add(group);
            updatePlayer(pp);
            return true;
        }
    }


    public boolean removeGroupFromPlayer(@NotNull String player, @NotNull String group){
        PermissionPlayer player2 = getPlayer(player);
        if (player2 == null) {
            return false;
        }

        List<IncludedAble> playerGroups = new ArrayList<>();

        playerGroups.addAll(player2.getGroups());
        if (playerGroups.stream().noneMatch(group2 -> group2.getGroup().equalsIgnoreCase(group))) {
            return false;
        }

        if (playerGroups.stream().filter(includedAble -> !includedAble.getGroup().equalsIgnoreCase(group)).toList().isEmpty()) {
            List<String> groups = new ArrayList<>();
                    groups.addAll(CloudPermissionAPI.getInstance().getGroups().stream().filter(PermissionGroup::getIsDefault).map(PermissionGroup::getGroup).toList());
            groups.forEach(permissionGroup ->      player2.getGroups().add(new IncludedAble(permissionGroup, "LIFETIME")));
        }

        playerGroups.removeIf(group2 -> group2.getGroup().equalsIgnoreCase(group));

        updatePlayer(player2);
        return true;

    }

    public ArrayList<PermissionGroup> getGroups(){
        return getConfig().getGroups();
    }

    public PermissionGroup getGroup(@NotNull String group){
        return getGroups().stream().filter(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group)).findFirst().orElse(null);
    }

    public boolean isDefault(@NotNull String group){
        return getGroup(group) != null && getGroup(group).getIsDefault();
    }

    public ArrayList<PermissionAble> getPermissionsFormGroup(@NotNull String group){
        ArrayList<PermissionAble> permissionAbles = new ArrayList<>(getGroup(group).getPermissions());
        getGroup(group).getIncluded().forEach(includedAble -> {
            permissionAbles.addAll(  getGroup(includedAble.getGroup()).getPermissions());
        });
        return permissionAbles;
    }

    public boolean excludeGroup(@NotNull String group ,@NotNull String excludedGroup){
        if (isGroupExists(group)){
            PermissionGroup permissionGroup = getGroup(group);
            if (permissionGroup.getIncluded().stream().noneMatch(includedAble1 -> includedAble1.getGroup().equalsIgnoreCase(excludedGroup))) return false;
            permissionGroup.getIncluded().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(excludedGroup));
            updateGroup(permissionGroup);
            return true;
        }
        return false;
    }

    public boolean includeGroup(@NotNull String group ,@NotNull IncludedAble includedAble){
        if (isGroupExists(group)){
            PermissionGroup permissionGroup = getGroup(group);
            if (permissionGroup.getIncluded().stream().anyMatch(includedAble1 -> includedAble1.getGroup().equalsIgnoreCase(includedAble.getGroup()))) return false;
            permissionGroup.getIncluded().add(includedAble);
            updateGroup(permissionGroup);
            return true;
        }
        return false;
    }

    public boolean updateGroup(@NotNull PermissionGroup group){
        if (isGroupExists(group.getGroup())){
            Configuration configuration = getConfig();
            if (configuration == null) return false;
            configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group.getGroup()));
            configuration.getGroups().add(group);
            updateConfig(configuration);
            return true;
        }
        return false;
    }

    public boolean removePermissionFromGroup(@NotNull String group, @NotNull String permission){
        if (isGroupExists(group)){
            Configuration configuration = getConfig();
            if (configuration == null) return false;
            PermissionGroup gupdate = configuration.getGroups().stream().filter(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group)).findFirst().orElse(null);
            if (gupdate == null) return false;
            if (gupdate.getPermissions().stream().noneMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))) return false;
            gupdate.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission));
            updateGroup(gupdate);
            return true;
        }
        return false;
    }

    public boolean addPermissionToGroup(@NotNull String group, @NotNull PermissionAble able){
        if (isGroupExists(group)){
            Configuration configuration = getConfig();
            if (configuration == null) return false;
            PermissionGroup gupdate = configuration.getGroups().stream().filter(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group)).findFirst().orElse(null);
            if (gupdate == null) return false;
            if (gupdate.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(able.getPermission()))) return false;
            gupdate.getPermissions().add(able);
            updateGroup(gupdate);
            return true;
        }
        return false;
    }

    public boolean deleteGroup(@NotNull String group){
        Configuration configuration = getConfig();
        if (configuration == null) return false;
        if (configuration.getGroups().stream().anyMatch(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group))){
            configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group));
            getPlayers().forEach(permissionPlayer -> removeGroupFromPlayer(Objects.requireNonNull(UUIDDriver.getUsername(permissionPlayer.getUuid())), group));
            updateConfig(configuration);
            return true;
        }
            return false;
    }

    public boolean isGroupExists(@NotNull String group){
        return getConfig().getGroups().stream().anyMatch(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group));
    }

    public boolean createGroup(@NotNull PermissionGroup group){
        Configuration configuration = getConfig();
        if (configuration == null) return false;
        if (configuration.getGroups().stream().noneMatch(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group.getGroup()))){
            configuration.getGroups().add(group);
            updateConfig(configuration);
            return true;
        }
        return false;
    }
}
