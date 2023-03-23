package eu.metacloudservice.moduleside.listeners;

import eu.metacloudservice.config.groups.GroupConfiguration;
import eu.metacloudservice.config.groups.entry.GroupEntry;
import eu.metacloudservice.config.player.PlayerConfiguration;
import eu.metacloudservice.config.player.entry.GivenGroup;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.EventHandler;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.listeners.CloudPlayerConnectedEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CloudListener implements ICloudListener {


    @EventHandler
    public void handle(CloudPlayerConnectedEvent event){
        if (!new File("./modules/permissions/database/" + event.getUniqueId()+ ".json").exists()){
            GroupConfiguration configuration = (GroupConfiguration) new ConfigDriver("./modules/permissions/groups.json").read(GroupConfiguration.class);
            List<GroupEntry> groups = configuration.getGroups().stream().filter(GroupEntry::isDefaultGroup).toList();

            ArrayList<GivenGroup> givenGroups = new ArrayList<>();
            for (GroupEntry group : groups) {
                GivenGroup givenGroup = new GivenGroup();
                givenGroup.setGroup(group.getName());
                givenGroup.setCancellationAt("LIFETIME");
                givenGroups.add(givenGroup);
            }
            PlayerConfiguration player = new PlayerConfiguration();
            player.setUUID(event.getUniqueId();
            player.setPermissions(new ArrayList<>());
            player.setGroups(givenGroups);

            new ConfigDriver("./modules/permissions/database/" + event.getUniqueId()+ ".json").save(player);
        }
    }
}
