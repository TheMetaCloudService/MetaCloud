/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.moduleside;

import eu.metacloudservice.Driver;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.Locations;
import eu.metacloudservice.config.enums.ClickAction;
import eu.metacloudservice.config.impli.ItemLayout;
import eu.metacloudservice.config.impli.NPCConfig;
import eu.metacloudservice.config.impli.NPCNamesLayout;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.module.extention.IModule;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MetaModule implements IModule {


    @Override
    public void load() {
        if (new File("./modules/npc/config.json").exists()) {
            ArrayList<NPCConfig> configs = new ArrayList<>();
            Driver.getInstance().getGroupDriver().getAll().forEach(group -> {
                if (!group.getGroupType().equalsIgnoreCase("PROXY")){
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("§8►  §7%online_players%§8/§7%max_players%");
                    lore.add("§8►  §7%service_motd%");
                    NPCConfig cc = new NPCConfig(group.getGroup(), true, ClickAction.CONNECT_RANDOM, ClickAction.OPEN_INVENTORY, new NPCNamesLayout(
                            "§8► §b§l%group_name%", "§7§3%online_players% playing",
                            "§8► §b§l%group_name%", "§cCurrently in Maintenance"),
                            new ItemLayout("lime_dye", "§8► §b§l%service_name%", lore),
                                    new ItemLayout("light_blue_dye",  "§8► §b§l%service_name%", lore),
                                            new ItemLayout("orange_dye",  "§8► §b§l%service_name%", lore));
                    configs.add(cc);
                }
            });
            new ConfigDriver("./modules/npc/config.json").save(new Configuration(configs));
        }

        if (!new File("./modules/npc/locations.json").exists()){
            Locations locations = new Locations(new ArrayList<>());
            new ConfigDriver("./modules/npc/locations.json").save(locations);
        }
        set();
        update();
    }

    @Override
    public void unload() {

    }

    @Override
    public void reload() {
        update();
    }

    public void set(){
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/npc/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/npc/config.json").read(Configuration.class))));
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/npc/locations", new ConfigDriver().convert(new ConfigDriver("./modules/npc/locations.json").read(Locations.class))));
    }

    public static void update(){
        try {
            Driver.getInstance().getWebServer().updateRoute("/module/npc/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/npc/config.json").read(Configuration.class)));
            Driver.getInstance().getWebServer().updateRoute("/module/npc/locations", new ConfigDriver().convert(new ConfigDriver("./modules/npc/locations.json").read(Locations.class)));
        }catch (Exception e){
        }
    }
}
