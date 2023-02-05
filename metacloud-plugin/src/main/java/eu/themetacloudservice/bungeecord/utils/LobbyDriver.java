package eu.themetacloudservice.bungeecord.utils;

import eu.themetacloudservice.bungeecord.CloudPlugin;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.groups.dummy.Group;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LobbyDriver {

    private ArrayList<LobbyEntry> lobby;
    public LobbyDriver() {
        lobby = new ArrayList<>();
    }


    public ArrayList<LobbyEntry> getLobby() {
        return lobby;
    }


    public LobbyEntry findMatchingLobby(ProxiedPlayer player) {
        if (this.lobby.isEmpty()) {
            return null;
        }
        List<LobbyEntry> entries = getLobby().stream().filter(lobbyEntry -> (lobbyEntry.getPermission().equals("") || player.hasPermission(lobbyEntry.getPermission()))).filter(lobbyEntry -> {
            Group group = (Group) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/" + lobbyEntry.getGroup()), Group.class);

            if (group.isMaintenance()){
                return false;
            }else {
                return true;
            }

        }).collect(Collectors.toList());
        if (entries.isEmpty()){
            return null;
        }
        List<Integer> priority = new ArrayList<>();
        entries.forEach(lobbyEntry -> priority.add(Integer.valueOf(lobbyEntry.getPriority())));
        priority.sort(Collections.reverseOrder());
        int priorty = priority.get(0);
        List<LobbyEntry> lobby = entries.stream().filter(lobbyEntry -> (lobbyEntry.getPriority() == priorty)).collect(Collectors.toList());
        if (lobby.size() == 0){
            return null;
        }
        return lobby.get((new Random()).nextInt(lobby.size()));
    }

        public LobbyEntry findMatchingLobby(ProxiedPlayer player, String kickedServer) {
        if (this.lobby.isEmpty()){
            return null;
        }
        List<LobbyEntry> entries = getLobby().stream().filter(lobbyEntry -> (lobbyEntry.getPermission().equals("") || player.hasPermission(lobbyEntry.getPermission()))).filter(lobbyEntry -> !lobbyEntry.getName().equalsIgnoreCase(kickedServer)).filter(lobbyEntry -> {
            Group group = (Group) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/" + lobbyEntry.getGroup()), Group.class);
            if (group.isMaintenance()){
                return false;
            }else {
                return true;
            }
        }).collect(Collectors.toList());
        if (entries.isEmpty()){
            return null;
        }
        List<Integer> priority = new ArrayList<>();
        entries.forEach(lobbyEntry -> priority.add(lobbyEntry.getPriority()));
        priority.sort(Collections.reverseOrder());
        int priorty = priority.get(0);
        List<LobbyEntry> lobby = entries.stream().filter(lobbyEntry -> (lobbyEntry.getPriority() == priorty)).collect(Collectors.toList());
        if (lobby.size() == 0){
            return null;
        }
        return lobby.get((new Random()).nextInt(lobby.size()));
    }


}
