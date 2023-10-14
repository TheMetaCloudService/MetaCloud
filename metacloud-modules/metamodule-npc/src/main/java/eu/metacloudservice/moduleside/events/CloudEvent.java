/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.moduleside.events;

import eu.metacloudservice.Driver;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.Locations;
import eu.metacloudservice.config.enums.ClickAction;
import eu.metacloudservice.config.impli.ItemLayout;
import eu.metacloudservice.config.impli.NPCConfig;
import eu.metacloudservice.config.impli.NPCNamesLayout;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Priority;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.group.CloudGroupCreateEvent;
import eu.metacloudservice.events.listeners.group.CloudGroupDeleteEvent;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIPutEvent;
import eu.metacloudservice.moduleside.MetaModule;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.out.service.PacketOutResAPItReload;

import java.util.ArrayList;

public class CloudEvent implements ICloudListener {


    @Subscribe(priority = Priority.HIGHEST)
    public void handle(CloudRestAPIPutEvent event){
        if (event.getPath().equals("/module/npc/locations")){
            new ConfigDriver("./modules/npc/locations.json").save(new ConfigDriver().convert(event.getContent(), Locations.class));
        }
    }

    @Subscribe(priority = Priority.HIGHEST)
    public void handle(CloudGroupCreateEvent event){
        if (!Driver.getInstance().getGroupDriver().load(event.getGroupname()).getGroupType().equalsIgnoreCase("PROXY")){
            Configuration configuration = (Configuration) new ConfigDriver("./modules/npc/config.json").read(Configuration.class);
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§8►  §7%online_players%§8/§7%max_players%");
            lore.add("§8►  §7%service_motd%");
            NPCConfig cc = new NPCConfig(event.getGroupname(), true, ClickAction.CONNECT_RANDOM, ClickAction.OPEN_INVENTORY, new NPCNamesLayout(
                    "§8► §b§l%group_name%", "§7§3%online_players% playing",
                    "§8► §b§l%group_name%", "§cCurrently in Maintenance"),
                    new ItemLayout("lime_dye", "§8► §b§l%service_name%", lore),
                    new ItemLayout("light_blue_dye",  "§8► §b§l%service_name%", lore),
                    new ItemLayout("orange_dye",  "§8► §b§l%service_name%", lore),"§7► §bCLOUDNPC §8• §7%group_name%");

            configuration.getConfigurations().add(cc);
            MetaModule.update();
            new ConfigDriver("./modules/npc/config.json").save(configuration);
            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutResAPItReload());
        }
    }

    @Subscribe(priority = Priority.HIGHEST)
    public void handle(CloudGroupDeleteEvent event){
        Configuration configuration = (Configuration) new ConfigDriver("./modules/npc/config.json").read(Configuration.class);
        configuration.configurations.removeIf(npcConfig -> npcConfig.targetGroup.equalsIgnoreCase(event.getGroupname()));
        MetaModule.update();
        new ConfigDriver("./modules/npc/config.json").save(configuration);
        NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutResAPItReload());
    }
}
