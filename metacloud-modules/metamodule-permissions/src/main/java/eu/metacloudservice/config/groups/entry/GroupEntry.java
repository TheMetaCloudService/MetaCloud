package eu.metacloudservice.config.groups.entry;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupEntry {

    private String name;
    private String prefix;
    private String suffix;

    private int tagPower;
    private boolean defaultGroup;
    private HashMap<String, Boolean> permissions;
    private HashMap<String, String> include;

    public String getName() {
        return name;
    }

    public int getTagPower() {
        return tagPower;
    }

    public void setTagPower(int tagPower) {
        this.tagPower = tagPower;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(boolean defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public HashMap<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(HashMap<String, Boolean> permissions) {
        this.permissions = permissions;
    }

    public HashMap<String, String> getInclude() {
        return include;
    }

    public void setInclude(HashMap<String, String> include) {
        this.include = include;
    }
}
