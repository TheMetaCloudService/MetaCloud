package eu.metacloudservice.moduleside;

import eu.metacloudservice.Driver;
import eu.metacloudservice.config.groups.GroupConfiguration;
import eu.metacloudservice.config.groups.entry.GroupEntry;
import eu.metacloudservice.config.player.PlayerConfiguration;
import eu.metacloudservice.config.player.entry.GivenGroup;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.listeners.CloudPlayerConnectedEvent;
import eu.metacloudservice.module.extention.IModule;
import eu.metacloudservice.moduleside.listeners.CloudListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MetaModule implements IModule {
    @Override
    public void load() {
        imnitConfigs();
        Driver.getInstance().getMessageStorage().eventDriver.registerListener(new CloudListener());

    }

    @Override
    public void unload() {

    }

    @Override
    public void reload() {

    }

    private void imnitConfigs(){

        if (!new File("./modules/permissions/database/").exists()){
            new File("./modules/permissions/database/").mkdirs();
        }

        File file = new File("./modules/permissions/database/");
        File[] files = file.listFiles();
        ArrayList<String> modules = new ArrayList<>();
        for (int i = 0; i != files.length; i++) {
            String FirstFilter = files[i].getName();
            if (FirstFilter.contains(".json")) {
                String group = FirstFilter.split(".json")[0];
                modules.add(group);
            }
        }

        modules.forEach(s -> {
            PlayerConfiguration configuration = (PlayerConfiguration) new ConfigDriver("./modules/permissions/database/"+ s+ ".json").read(PlayerConfiguration.class);
        });

        if (!new File("./modules/permissions/groups.json").exists()){

         GroupConfiguration configuration = new GroupConfiguration();
         ArrayList<GroupEntry> groups = new ArrayList<>();

            GroupEntry admin = new GroupEntry();
            ArrayList<String> includes = new ArrayList<>();
            ArrayList<String> perms = new ArrayList<>();
            perms.add("*");
            includes.add("member");
            admin.setName("admin");
            admin.setPrefix("§bAdmin §8| §7");
            admin.setSuffix("");
            admin.setInclude(includes);
            admin.setTagPower(0);
            admin.setPermissions(perms);
            admin.setDefaultGroup(false);

         GroupEntry defaultGroup = new GroupEntry();
         defaultGroup.setName("member");
         defaultGroup.setPrefix("§7Member §8| §7");
         defaultGroup.setSuffix("");
         defaultGroup.setInclude(new ArrayList<>());
         defaultGroup.setTagPower(999);
         defaultGroup.setPermissions(new ArrayList<>());
         defaultGroup.setDefaultGroup(true);
            groups.add(admin);
            groups.add(defaultGroup);

            configuration.setGroups(groups);
            new ConfigDriver("./modules/permissions/groups.json").save(configuration);


        }


    }



    public static void  initPlayer(CloudPlayerConnectedEvent event){
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
            player.setUUID(event.getUniqueId());
            player.setPermissions(new ArrayList<>());
            player.setGroups(givenGroups);

            new ConfigDriver("./modules/permissions/database/" + event.getUniqueId()+ ".json").save(player);
        }
    }

}
