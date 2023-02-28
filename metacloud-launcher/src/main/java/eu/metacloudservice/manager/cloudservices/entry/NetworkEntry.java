package eu.metacloudservice.manager.cloudservices.entry;

import java.util.HashMap;

public class NetworkEntry {



    public Integer global_players;
    public Integer global_player_potency;
    public HashMap<String, Integer> group_player_potency;

    public NetworkEntry() {
        group_player_potency = new HashMap<>();
        global_player_potency = 0;
        global_players = 0;
    }


}
