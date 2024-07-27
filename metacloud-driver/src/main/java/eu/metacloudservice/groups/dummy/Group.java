package eu.metacloudservice.groups.dummy;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.Getter;
import lombok.NonNull;
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
    @Setter
    @Getter
    private Integer priority;

    @Setter
    @Getter
    private Integer startPriority;
    @Getter
    @Setter
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
    @Setter
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

    public Group(@NonNull final String group, @NonNull final String groupType, @NonNull final Integer usedMemory, @NonNull final boolean maintenance, @NonNull final boolean runStatic, @NonNull final Integer priority, @NonNull final String permission, @NonNull final Integer maxPlayers, @NonNull final Integer minimalOnline, Integer maximalOnline, @NonNull final Integer startNewPercent, @NonNull final Integer over100AtGroup, @NonNull final Integer over100AtNetwork, @NonNull final GroupStorage storage, @NonNull final int startPriority) {
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
        this.startPriority = startPriority;
    }

}
