package eu.themetacloudservice.groups.dummy;

import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;

public class Group implements IConfigAdapter {

    private String group;
    private String groupType;
    private Integer usedMemory;
    private boolean maintenance;
    private boolean runStatic;
    private Integer maxPlayers;
    private Integer startNewPercent;
    private Integer over100AtGroup;
    private Integer over100AtNetwork;
    private GroupStorage storage;


    public Group() {}

    public Group(String group, String groupType, Integer usedMemory, boolean maintenance, boolean runStatic, Integer maxPlayers, Integer startNewPercent, Integer over100AtGroup, Integer over100AtNetwork, GroupStorage storage) {
        this.group = group;
        this.groupType = groupType;
        this.usedMemory = usedMemory;
        this.maintenance = maintenance;
        this.runStatic = runStatic;
        this.maxPlayers = maxPlayers;
        this.startNewPercent = startNewPercent;
        this.over100AtGroup = over100AtGroup;
        this.over100AtNetwork = over100AtNetwork;
        this.storage = storage;
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
}
