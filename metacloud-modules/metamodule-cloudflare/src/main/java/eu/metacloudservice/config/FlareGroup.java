/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.config;


import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class FlareGroup {

    private String group;
    private String sub;
    private int priority;
    private int weight;

    public FlareGroup(String group, String sub, int priority, int weight) {
        this.group = group;
        this.sub = sub;
        this.priority = priority;
        this.weight = weight;
    }

    public FlareGroup() {
    }
}
