package eu.metacloudservice.groups.dummy;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.Getter;
import lombok.Setter;

public class Group implements IConfigAdapter {
    @Getter
    private String group;
    @Getter
    private String groupType;
    @Getter
    private Integer usedMemory;
    @Getter
    @Setter
    private boolean maintenance;
    @Getter
    private boolean runStatic;
    @Getter
    private Integer priority;
    @Getter
    private String permission;
    @Setter
    @Getter
    private Integer maxPlayers;
    @Setter
    @Getter
    private Integer minimalOnline;
    @Setter
    @Getter
    private Integer  maximalOnline;
    @Getter
    private Integer startNewPercent;
    @Getter
    private Integer over100AtGroup;
    @Getter
    private Integer over100AtNetwork;
    @Setter
    @Getter
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

}
