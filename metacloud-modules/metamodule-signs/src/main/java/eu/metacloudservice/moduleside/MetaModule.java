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

public class
MetaModule implements IModule {

    @Override
    public void load() {
        create();
        set();
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

            ArrayList<SignLayout> online = new ConfigBuilder()
                    .add("stained_hardened_clay", "5", "§8► %service_name% §8◄", "§bLOBBY §8| §b§l✔", "%service_motd%", "§8• %online_players% / %max_players% §8•")
                    .add("stained_hardened_clay", "5", "§8► %service_name% §8◄", "§3L§bOBBY §8| §b§l✔", "%service_motd%", "§8• %online_players% / %max_players% §8•")
                    .add("stained_hardened_clay", "5", "§8► %service_name% §8◄", "§bL§3O§bBBY §8| §b§l✔", "%service_motd%", "§8• %online_players% / %max_players% §8•")
                    .add("stained_hardened_clay", "5", "§8► %service_name% §8◄", "§bLO§3B§bBY §8| §b§l✔", "%service_motd%", "§8• %online_players% / %max_players% §8•")
                    .add("stained_hardened_clay", "5", "§8► %service_name% §8◄", "§bLOB§3B§bY §8| §b§l✔", "%service_motd%", "§8• %online_players% / %max_players% §8•")
                    .add("stained_hardened_clay", "5", "§8► %service_name% §8◄", "§bLOBB§3Y §8| §b§l✔", "%service_motd%", "§8• %online_players% / %max_players% §8•")
                    .build();

            ArrayList<SignLayout> full= new ConfigBuilder()
                    .add("stained_hardened_clay", "4", "§8► %service_name% §8◄", "§eVIP §8| §e§l✘", "%service_motd%", "§8• %online_players% / %max_players% §8•")
                    .add("stained_hardened_clay", "4", "§8► %service_name% §8◄", "§6V§eIP §8| §e§l✘", "%service_motd%", "§8• %online_players% / %max_players% §8•")
                    .add("stained_hardened_clay", "4", "§8► %service_name% §8◄", "§eV§6I§eP §8| §e§l✘", "%service_motd%", "§8• %online_players% / %max_players% §8•")
                    .add("stained_hardened_clay", "4", "§8► %service_name% §8◄", "§eVI§6P §8| §e§l✘", "%service_motd%", "§8• %online_players% / %max_players% §8•")
                    .build();

            ArrayList<SignLayout> maintenance= new ConfigBuilder()
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§cMAINTENANCE", "")
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§4M§cAINTENANCE", "")
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§cM§4A§cINTENANCE", "")
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§cMA§4I§cNTENANCE", "")
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§cMAI§4N§cTENANCE", "")
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§cMAIN§4T§cENANCE", "")
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§cMAINT§4E§cNANCE", "")
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§cMAINTE§4N§cANCE", "")
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§cMAINTEN§4A§cNCE", "")
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§cMAINTENA§4N§cCE", "")
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§cMAINTENAN§4C§cE", "")
                    .add("stained_hardened_clay", "14", "", "%service_group_name%", "§cMAINTENANC§4E", "")
                    .build();

            ArrayList<SignLayout> searching= new ConfigBuilder()
                    .add("stained_hardened_clay", "9", "", "searching for Services", "§co O o", "")
                    .add("stained_hardened_clay", "9", "", "searching for Services", "§co o O", "")
                    .add("stained_hardened_clay", "9", "", "searching for Services", "§co O o", "")
                    .add("stained_hardened_clay", "9", "", "searching for Services", "§cO o o", "")
                    .build();


            Configuration configuration = new Configuration(true, online, full, maintenance, searching);
            new ConfigDriver("./modules/signs/config.json").save(configuration);

        }
        if (!new File("./modules/signs/locations.json").exists()){
            Locations locations = new Locations(new ArrayList<>());
            new ConfigDriver("./modules/signs/locations.json").save(locations);
        }
    }

    public void set(){

            General general = new General("signs", "1.0.0", "RauchigesEtwas");
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/signs/general", new ConfigDriver().convert(general)));
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/signs/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/signs/config.json").read(Configuration.class))));
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/signs/locations", new ConfigDriver().convert(new ConfigDriver("./modules/signs/locations.json").read(Locations.class))));
    }

    public void update(){
        try {
            Driver.getInstance().getWebServer().updateRoute("/module/signs/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/signs/config.json").read(Configuration.class)));
            Driver.getInstance().getWebServer().updateRoute("/module/signs/locations", new ConfigDriver().convert(new ConfigDriver("./modules/signs/locations.json").read(Configuration.class)));
        }catch (Exception e){
        }
    }



}
