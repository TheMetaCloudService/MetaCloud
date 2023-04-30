package eu.metacloudservice.api;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.dummy.Permission;
import eu.metacloudservice.api.interfaces.ICloudPermsPool;
import eu.metacloudservice.config.groups.GroupConfiguration;
import eu.metacloudservice.config.groups.entry.GroupEntry;
import eu.metacloudservice.config.player.PlayerConfiguration;
import eu.metacloudservice.config.player.entry.GivenGroup;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.storage.UUIDDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CloudPermsPool implements ICloudPermsPool {

    private static  CloudPermsPool instance;

    public CloudPermsPool() {
        instance = this;
    }

    public static CloudPermsPool getInstance() {
        return instance;
    }

    @Override
    public List<Permission> getPermissionOfGroup(String group) {
        GroupEntry entry = getGroup(group);
        List<GroupEntry> given = getGivenGroupsOfGroup(group);
        List<Permission> permissions = new ArrayList<>();
        entry.getPermissions().forEach((s, options) -> {
            permissions.add(new Permission(s, (String) options.get("cancellationAt"), (Boolean) options.get("set")));
        });
        given.forEach(groupEntry -> {
            groupEntry.getPermissions().forEach((s, options) -> {
                permissions.add(new Permission(s, (String) options.get("cancellationAt"), (Boolean) options.get("set")));
            });
        });
        return permissions;
    }

    @Override
    public GroupEntry getGroup(String group) {
        GroupConfiguration convert = (GroupConfiguration) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/module/permissions/configuration"), GroupConfiguration.class);
        return convert.getGroups().stream().filter(groupEntry -> groupEntry.getName().equalsIgnoreCase(group)).findFirst().orElse(null);
    }

    @Override
    public List<GroupEntry> getGivenGroupsOfGroup(String group) {
        GroupConfiguration convert = (GroupConfiguration) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/module/permissions/configuration"), GroupConfiguration.class);
        List<String> groups = convert.getGroups().stream().filter(groupEntry -> groupEntry.getName().equalsIgnoreCase(group)).findFirst().get().getInclude().keySet().stream().toList();
        return convert.getGroups().stream().filter(groupEntry -> groups.contains(groupEntry.getName())).collect(Collectors.toList());

    }

    @Override
    public List<Permission> getPermissionOfPlayer(String player) {
        List<GroupEntry> groups = getGroupsOfPlayer(player);
        List<Permission> permissions = new ArrayList<>();
        groups.forEach(groupEntry -> {
            permissions.addAll(getPermissionOfGroup(groupEntry.getName()));
        });

        return permissions;
    }

    @Override
    public List<GroupEntry> getGroupsOfPlayer(String player) {
        PlayerConfiguration players = (PlayerConfiguration) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/module/permissions/" + UUIDDriver.getUUID(player)), PlayerConfiguration.class);
        GroupConfiguration group = (GroupConfiguration) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/module/permissions/configuration"), GroupConfiguration.class);
        return group.getGroups().stream()
                .filter(groupEntry -> players.getGroups().stream()
                        .map(GivenGroup::getGroup)
                        .toList()
                        .contains(groupEntry.getName()))
                .toList();
    }

    @Override
    public PlayerConfiguration getPlayer(String player) {
        return (PlayerConfiguration) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/module/permissions/" + UUIDDriver.getUUID(player)), PlayerConfiguration.class);
    }
}
