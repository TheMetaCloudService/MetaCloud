/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bukkit.events;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.bukkit.NPCBootstrap;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.Locations;
import eu.metacloudservice.config.impli.NPCConfig;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIReloadEvent;
import eu.metacloudservice.process.ServiceState;
import org.bukkit.ChatColor;


import java.util.Objects;

public class CloudEvents implements ICloudListener {

    @Subscribe
    public void handle(CloudRestAPIReloadEvent event){
        System.out.println("TEST#1");
        NPCBootstrap.getInstance().npcAPI.configuration = ((Configuration) new ConfigDriver().convert( CloudAPI.getInstance().getRestDriver().get("/module/npc/configuration"), Configuration.class));
        NPCBootstrap.getInstance().npcAPI.Locations = ((Locations) new ConfigDriver().convert( CloudAPI.getInstance().getRestDriver().get("/module/npc/locations"), Locations.class));

        NPCBootstrap.getInstance().npcAPI.getLocConfig().getLocations().forEach(npcLocation -> {
            NPC.Global npc = NPCLib.getInstance().getAllGlobalNPCs().stream().filter(global -> global.getSimpleID().equalsIgnoreCase(npcLocation.getNpcUUID())).findFirst().orElse(null);
            NPCConfig config = NPCBootstrap.getInstance().npcAPI.configuration.getConfigurations().stream().filter(npcConfig -> npcConfig.targetGroup.equalsIgnoreCase(npcLocation.getGroupName())).findFirst().orElse(null);

            npc.setTabListVisibility(NPC.TabListVisibility.NEVER);
            if (!config.glowColor.isEmpty()){
                npc.setGlowing(true, ChatColor.valueOf(config.glowColor));
            }
            npc.setPose(NPC.Pose.STANDING);
            if (config.tracingPlayers)
                npc.setGazeTrackingType(NPC.GazeTrackingType.PLAYER);
            npc.setCollidable(config.collidable);
            npc.setInteractCooldown(20);

            if (npcLocation.isSkinOwnerName()){
                npc.setOwnPlayerSkin(true);
            }else {
                npc.setSkin(npcLocation.getSkinOwner());
            }

            if (CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).isMaintenance()){
                npc.setText(config.names.maintenanceLine1
                                .replace("&", "§")
                                .replace("%group_name%", npcLocation.getGroupName())
                                .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()),
                        config.names.maintenanceLine2       .replace("&", "§")     .replace("%group_name%", npcLocation.getGroupName())
                                .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()));
            }else {
                npc.setText(config.names.defaultLine1         .replace("&", "§")   .replace("%group_name%", npcLocation.getGroupName())
                                .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()),
                        config.names.defaultLine2         .replace("&", "§")   .replace("%group_name%", npcLocation.getGroupName())
                                .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()));
            }
            npc.forceUpdate();
            npc.hide();
            npc.show();
        });


    }
}
