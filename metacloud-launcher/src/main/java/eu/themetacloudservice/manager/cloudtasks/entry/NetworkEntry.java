package eu.themetacloudservice.manager.cloudtasks.entry;

import java.util.HashMap;

public class NetworkEntry {



    public Integer global_players;
    public Integer global_player_potency;
    public HashMap<String, Integer> group_player_potency;

    public NetworkEntry() {
        group_player_potency = new HashMap<>();
    }


}
