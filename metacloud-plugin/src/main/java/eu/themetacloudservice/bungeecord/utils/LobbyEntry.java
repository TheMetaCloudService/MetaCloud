package eu.themetacloudservice.bungeecord.utils;

public class LobbyEntry {
    private final String name;
    private final String permission;
    private String group;
    private final int priority;
    private int players;

    public LobbyEntry(String name, String permission, int priority, String group) {
        this.name = name;
        this.permission = permission;
        this.priority = priority;
        this.players = 0;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public int getPriority() {
        return priority;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public String getGroup() {
        return group;
    }


}
