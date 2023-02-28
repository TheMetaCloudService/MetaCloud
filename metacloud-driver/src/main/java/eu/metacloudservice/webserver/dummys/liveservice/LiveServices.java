package eu.metacloudservice.webserver.dummys.liveservice;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

public class LiveServices implements IConfigAdapter {


    private String name;
    private String group;
    private int players;

    private boolean isInGame;

    public LiveServices() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }


    public boolean isInGame() {
        return isInGame;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
