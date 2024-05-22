package eu.metacloudservice.moduleside.events;

import eu.metacloudservice.Driver;
import eu.metacloudservice.config.*;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Priority;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.group.CloudGroupCreateEvent;
import eu.metacloudservice.events.listeners.group.CloudGroupDeleteEvent;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIUpdateEvent;
import eu.metacloudservice.moduleside.MetaModule;
import eu.metacloudservice.networking.NettyDriver;

import java.util.ArrayList;

public class RestUpdate implements ICloudListener {


    @Subscribe(priority = Priority.HIGHEST)
    public void handle(CloudRestAPIUpdateEvent event){
        if (event.getPath().equals("/module/signs/locations")){
            new ConfigDriver("./modules/signs/locations.json").save(new ConfigDriver().convert(event.getContent(), Locations.class));
        }
    }

    @Subscribe(priority = Priority.HIGHEST)
    public void handle(CloudGroupCreateEvent event){
        if (!Driver.getInstance().getGroupDriver().load(event.getGroupname()).getGroupType().equalsIgnoreCase("PROXY")){
            Configuration configuration = (Configuration) new ConfigDriver("./modules/signs/config.json").read(Configuration.class);
            ArrayList<SignLayout> empty = new ConfigBuilder()
                    .add("green_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§bEMPTY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("green_glazed_terracotta",  "","§8► §7%service_name% §8◄", "§3E§bMPTY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("green_glazed_terracotta",  "","§8► §7%service_name% §8◄", "§bE§3M§bPTY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("green_glazed_terracotta",  "","§8► §7%service_name% §8◄", "§bEM§3P§bTY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("green_glazed_terracotta",  "","§8► §7%service_name% §8◄", "§bEMP§3T§bY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("green_glazed_terracotta",  "","§8► §7%service_name% §8◄", "§bEMPT§3Y §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .build();


            ArrayList<SignLayout> online = new ConfigBuilder()
                    .add("lime_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§bLOBBY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("lime_glazed_terracotta",  "","§8► §7%service_name% §8◄", "§3L§bOBBY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("lime_glazed_terracotta",  "","§8► §7%service_name% §8◄", "§bL§3O§bBBY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("lime_glazed_terracotta",  "","§8► §7%service_name% §8◄", "§bLO§3B§bBY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("lime_glazed_terracotta",  "","§8► §7%service_name% §8◄", "§bLOB§3B§bY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("lime_glazed_terracotta",  "","§8► §7%service_name% §8◄", "§bLOBB§3Y §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .build();

            ArrayList<SignLayout> full= new ConfigBuilder()
                    .add("yellow_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§eVIP §8| §e§l✘", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("yellow_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§6V§eIP §8| §e§l✘", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("yellow_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§eV§6I§eP §8| §e§l✘", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .add("yellow_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§eVI§6P §8| §e§l✘", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                    .build();

            ArrayList<SignLayout> maintenance= new ConfigBuilder()
                    .add("red_glazed_terracotta",  "","", "§8► §7%service_group_name% §8◄", "§cMAINTENANCE", "")
                    .add("red_glazed_terracotta",   "","", "§8► §7%service_group_name% §8◄", "§4M§cAINTENANCE", "")
                    .add("red_glazed_terracotta",   "","", "§8► §7%service_group_name% §8◄", "§cM§4A§cINTENANCE", "")
                    .add("red_glazed_terracotta",   "","", "§8► §7%service_group_name% §8◄", "§cMA§4I§cNTENANCE", "")
                    .add("red_glazed_terracotta",   "","","§8► §7%service_group_name% §8◄", "§cMAI§4N§cTENANCE", "")
                    .add("red_glazed_terracotta",   "","","§8► §7%service_group_name% §8◄", "§cMAIN§4T§cENANCE", "")
                    .add("red_glazed_terracotta",   "","", "§8► §7%service_group_name% §8◄", "§cMAINT§4E§cNANCE", "")
                    .add("red_glazed_terracotta",   "","", "§8► §7%service_group_name% §8◄", "§cMAINTE§4N§cANCE", "")
                    .add("red_glazed_terracotta",  "", "", "§8► §7%service_group_name% §8◄", "§cMAINTEN§4A§cNCE", "")
                    .add("red_glazed_terracotta",  "","", "§8► §7%service_group_name% §8◄", "§cMAINTENA§4N§cCE", "")
                    .add("red_glazed_terracotta",   "","", "§8► §7%service_group_name% §8◄", "§cMAINTENAN§4C§cE", "")
                    .add("red_glazed_terracotta",   "","", "§8► §7%service_group_name% §8◄", "§cMAINTENANC§4E", "")
                    .build();

            ArrayList<SignLayout> searching= new ConfigBuilder()
                    .add("gray_glazed_terracotta",   "","", "searching for Services", "§co O o", "")
                    .add("gray_glazed_terracotta",   "","", "searching for Services", "§co o O", "")
                    .add("gray_glazed_terracotta",   "","", "searching for Services", "§co O o", "")
                    .add("gray_glazed_terracotta",   "","", "searching for Services", "§cO o o", "")
                    .build();
            SignConfig signConfig = new SignConfig(event.getGroupname(),true, true,   1.0, 0.8 ,empty, online, full, maintenance, searching);

            configuration.configurations.add(signConfig);
            MetaModule.update();
            new ConfigDriver("./modules/signs/config.json").save(configuration);

            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutResAPItReload());
        }
    }

    @Subscribe(priority = Priority.HIGHEST)
    public void handle(CloudGroupDeleteEvent event){
        Configuration configuration = (Configuration) new ConfigDriver("./modules/signs/config.json").read(Configuration.class);

        configuration.configurations.removeIf(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(event.getGroupname()));
        MetaModule.update();
        new ConfigDriver("./modules/signs/config.json").save(configuration);
        NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutResAPItReload());
    }

}
