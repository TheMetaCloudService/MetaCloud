package eu.themetacloudservice.groups.dummy;

import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;

public class Group implements IConfigAdapter {

    private String group;
    private String groupType;
    private Integer usedMemory;
    private boolean maintenance;
    private boolean runStatic;

    private Integer priority;
    private String permission;
    private Integer maxPlayers;
    private Integer minimalOnline;
    private Integer  maximalOnline;
    private Integer startNewPercent;
    private Integer over100AtGroup;
    private Integer over100AtNetwork;
    private GroupStorage storage;


    public Group() {}

    public Group(String group, String groupType, Integer usedMemory, boolean maintenance, boolean runStatic, Integer priority, String permission, Integer maxPlayers, Integer minimalOnline, Integer maximalOnline, Integer startNewPercent, Integer over100AtGroup, Integer over100AtNetwork, GroupStorage storage) {
        this.group = group;
        this.groupType = groupType;
        this.usedMemory = usedMemory;
        this.maintenance = maintenance;
        this.runStatic = runStatic;
        this.priority = priority;
        this.permission = permission;
        this.maxPlayers = maxPlayers;
        this.minimalOnline = minimalOnline;
        this.maximalOnline = maximalOnline;
        this.startNewPercent = startNewPercent;
        this.over100AtGroup = over100AtGroup;
        this.over100AtNetwork = over100AtNetwork;
        this.storage = storage;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setMinimalOnline(Integer minimalOnline) {
        this.minimalOnline = minimalOnline;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getPermission() {
        return permission;
    }

    public Integer getMinimalOnline() {
        return minimalOnline;
    }

    public Integer getMaximalOnline() {
        return maximalOnline;
    }

    public String getGroup() {
        return group;
    }

    public String getGroupType() {
        return groupType;
    }

    public Integer getUsedMemory() {
        return usedMemory;
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public boolean isRunStatic() {
        return runStatic;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public Integer getStartNewPercent() {
        return startNewPercent;
    }

    public Integer getOver100AtGroup() {
        return over100AtGroup;
    }

    public Integer getOver100AtNetwork() {
        return over100AtNetwork;
    }

    public GroupStorage getStorage() {
        return storage;
    }

    public void setStorage(GroupStorage storage) {
        this.storage = storage;
    }
}
