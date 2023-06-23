package eu.metacloudservice.groups.dummy;

import lombok.Getter;
import lombok.Setter;

public class GroupStorage {

    @Getter
    @Setter
    private String template;
    @Getter
    @Setter
    private String runningNode;
    @Getter
    @Setter
    private String javaEnvironment;

    public GroupStorage() {
    }

    public GroupStorage(String template, String runningNode, String javaEnvironment) {
        this.template = template;
        this.runningNode = runningNode;
        this.javaEnvironment = javaEnvironment;
    }
}
