package eu.metacloudservice.moduleside;

import eu.metacloudservice.Driver;
import eu.metacloudservice.config.*;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.module.extention.IModule;
import eu.metacloudservice.moduleside.events.RestUpdate;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MetaModule implements IModule {

    @Override
    public void load() {
        create();
        set();
        update();
        Driver.getInstance().getMessageStorage().eventDriver.registerListener(new RestUpdate());
    }
    @Override
    public void unload() {
        create();
    }
    @Override
    public void reload() {
        create();
        set();
        update();

    }


    public void create(){
        if (!new File("./modules/signs/config.json").exists()){
            new File("./modules/signs/").mkdirs();

            ArrayList<SignConfig> configs = new ArrayList<>();
            Driver.getInstance().getGroupDriver().getAll().forEach(group -> {
            if (!group.getGroupType().equalsIgnoreCase("PROXY")){
                ArrayList<SignLayout> empty = new ConfigBuilder()
                        .add("green_glazed_terracotta",    "","§8► §7%service_name% §8◄", "§bEMPTY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("green_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§3E§bMPTY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("green_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§bE§3M§bPTY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("green_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§bEM§3P§bTY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("green_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§bEMP§3T§bY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("green_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§bEMPT§3Y §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .build();


                ArrayList<SignLayout> online = new ConfigBuilder()
                        .add("lime_glazed_terracotta",    "","§8► §7%service_name% §8◄", "§bLOBBY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("lime_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§3L§bOBBY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("lime_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§bL§3O§bBBY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("lime_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§bLO§3B§bBY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("lime_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§bLOB§3B§bY §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("lime_glazed_terracotta",   "","§8► §7%service_name% §8◄", "§bLOBB§3Y §8| §b§l✔", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .build();

                ArrayList<SignLayout> full= new ConfigBuilder()
                        .add("yellow_glazed_terracotta",    "","§8► §7%service_name% §8◄", "§eVIP §8| §e§l✘", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("yellow_glazed_terracotta",    "","§8► §7%service_name% §8◄", "§6V§eIP §8| §e§l✘", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("yellow_glazed_terracotta",    "","§8► §7%service_name% §8◄", "§eV§6I§eP §8| §e§l✘", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .add("yellow_glazed_terracotta",    "","§8► §7%service_name% §8◄", "§eVI§6P §8| §e§l✘", "%service_motd%", "§8• §7%online_players% / %max_players% §8•")
                        .build();

                ArrayList<SignLayout> maintenance= new ConfigBuilder()
                        .add("red_glazed_terracotta",   "","", "§8► §7%service_group_name% §8◄", "§cMAINTENANCE", "")
                        .add("red_glazed_terracotta",    "","", "§8► §7%service_group_name% §8◄", "§4M§cAINTENANCE", "")
                        .add("red_glazed_terracotta",    "","", "§8► §7%service_group_name% §8◄", "§cM§4A§cINTENANCE", "")
                        .add("red_glazed_terracotta",    "","", "§8► §7%service_group_name% §8◄", "§cMA§4I§cNTENANCE", "")
                        .add("red_glazed_terracotta",    "","","§8► §7%service_group_name% §8◄", "§cMAI§4N§cTENANCE", "")
                        .add("red_glazed_terracotta",    "","","§8► §7%service_group_name% §8◄", "§cMAIN§4T§cENANCE", "")
                        .add("red_glazed_terracotta",    "","", "§8► §7%service_group_name% §8◄", "§cMAINT§4E§cNANCE", "")
                        .add("red_glazed_terracotta",    "","", "§8► §7%service_group_name% §8◄", "§cMAINTE§4N§cANCE", "")
                        .add("red_glazed_terracotta",   "", "", "§8► §7%service_group_name% §8◄", "§cMAINTEN§4A§cNCE", "")
                        .add("red_glazed_terracotta",   "","", "§8► §7%service_group_name% §8◄", "§cMAINTENA§4N§cCE", "")
                        .add("red_glazed_terracotta",    "","", "§8► §7%service_group_name% §8◄", "§cMAINTENAN§4C§cE", "")
                        .add("red_glazed_terracotta",    "","", "§8► §7%service_group_name% §8◄", "§cMAINTENANC§4E", "")
                        .build();

                ArrayList<SignLayout> searching= new ConfigBuilder()
                        .add("gray_glazed_terracotta",    "","", "searching for Services", "§co O o", "")
                        .add("gray_glazed_terracotta",    "","", "searching for Services", "§co o O", "")
                        .add("gray_glazed_terracotta",    "","", "searching for Services", "§co O o", "")
                        .add("gray_glazed_terracotta",    "","", "searching for Services", "§cO o o", "")
                        .build();
                SignConfig signConfig = new SignConfig(group.getGroup(),true, true,   1.0, 0.8 ,empty, online, full, maintenance, searching);
                configs.add(signConfig);
            }
            });

            Configuration configuration = new Configuration(configs);
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/signs/configuration", new ConfigDriver().convert(configuration)));

            new ConfigDriver("./modules/signs/config.json").save(configuration);


        }
        if (!new File("./modules/signs/locations.json").exists()){
            Locations locations = new Locations(new ArrayList<>());
            new ConfigDriver("./modules/signs/locations.json").save(locations);
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/signs/locations", new ConfigDriver().convert(new ConfigDriver("./modules/signs/locations.json").read(Locations.class))));        }
        set();
        update();
    }

    public void set(){
        if ( !Driver.getInstance().getWebServer().isContentExists("/module/signs/configuration")){
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/signs/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/signs/config.json").read(Configuration.class))));

        }
        if ( !Driver.getInstance().getWebServer().isContentExists("/module/signs/locations")){
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/signs/locations", new ConfigDriver().convert(new ConfigDriver("./modules/signs/locations.json").read(Locations.class))));
        }
    }

    public static void update(){
        try {
            Driver.getInstance().getWebServer().updateRoute("/module/signs/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/signs/config.json").read(Configuration.class)));
            Driver.getInstance().getWebServer().updateRoute("/module/signs/locations", new ConfigDriver().convert(new ConfigDriver("./modules/signs/locations.json").read(Locations.class)));
        }catch (Exception e){
        }
    }



}
