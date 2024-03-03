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
import eu.metacloudservice.moduleside.events.CloudEvent;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MetaModule implements IModule {


    @Override
    public void load() {
        if (!new File("./local/GLOBAL/EVERY_LOBBY/plugins/npc-api.jar").exists()){
            try (BufferedInputStream in = new BufferedInputStream(new URL("https://metacloudservice.eu/download/npc-api.jar").openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream("./local/GLOBAL/EVERY_LOBBY/plugins/npc-api.jar")) {
                byte[] dataBuffer = new byte[1024];

                int bytesRead;

                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (!new File("./modules/npc/config.json").exists()) {
            new File("./modules/npc/").mkdirs();
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
                                            new ItemLayout("orange_dye",  "§8► §b§l%service_name%", lore), "§7► §bCLOUDNPC §8• §7%group_name%");
                    configs.add(cc);
                }
            });
            Configuration c =new Configuration(configs);
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/npc/configuration", new ConfigDriver().convert(c)));
            new ConfigDriver("./modules/npc/config.json").save(c);

        }

        if (!new File("./modules/npc/locations.json").exists()){
            Locations locations = new Locations(new ArrayList<>());
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/npc/locations", new ConfigDriver().convert(locations)));

            new ConfigDriver("./modules/npc/locations.json").save(locations);
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/npc/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/npc/config.json").read(Configuration.class))));

        }
        set();
        Driver.getInstance().getMessageStorage().eventDriver.registerListener(new CloudEvent());
        update();
    }

    @Override
    public void unload() {

    }

    @Override
    public void reload() {
        if (!new File("./modules/npc/config.json").exists()) {
            new File("./modules/npc/").mkdirs();
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
                            new ItemLayout("orange_dye",  "§8► §b§l%service_name%", lore), "§7► §bCLOUDNPC §8• §7%group_name%");
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

    public void set(){
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/npc/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/npc/config.json").read(Configuration.class))));
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/npc/locations", new ConfigDriver().convert(new ConfigDriver("./modules/npc/locations.json").read(Locations.class))));
    }

    public static void update(){
        Driver.getInstance().getWebServer().updateRoute("/module/npc/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/npc/config.json").read(Configuration.class)));
        Driver.getInstance().getWebServer().updateRoute("/module/npc/locations", new ConfigDriver().convert(new ConfigDriver("./modules/npc/locations.json").read(Locations.class)));

    }
}
