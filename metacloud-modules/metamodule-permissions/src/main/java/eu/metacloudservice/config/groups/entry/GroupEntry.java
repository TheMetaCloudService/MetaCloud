package eu.metacloudservice.config.groups.entry;

import java.util.ArrayList;

public class GroupEntry {

    private String name;
    private String prefix;
    private String suffix;

    private int tagPower;
    private boolean defaultGroup;
    private ArrayList<String> permissions;
    private ArrayList<String> include;

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

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public ArrayList<String> getInclude() {
        return include;
    }

    public void setInclude(ArrayList<String> include) {
        this.include = include;
    }
}
