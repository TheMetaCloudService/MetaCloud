/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bukkit;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.config.impli.NPCConfig;
import eu.metacloudservice.config.impli.NPCLocation;
import eu.metacloudservice.process.ServiceState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.BiConsumer;

public class WorkerTask extends BukkitRunnable {
    @Override
    public void run() {
        NPCBootstrap.getInstance().npcAPI.getLocConfig().getLocations().forEach(npcLocation -> {
            if (NPCBootstrap.getInstance().npcAPI.configuration.getConfigurations().stream().anyMatch(npcConfig -> npcConfig.targetGroup.equalsIgnoreCase(npcLocation.getGroupName()))){
                NPCConfig config = NPCBootstrap.getInstance().npcAPI.configuration.getConfigurations().stream().filter(npcConfig -> npcConfig.targetGroup.equalsIgnoreCase(npcLocation.getGroupName())).findFirst().orElse(null);
                if (!NPCLib.getInstance().hasGlobalNPC(NPCBootstrap.getInstance(), npcLocation.getNpcUUID())){
                    NPC.Global npc = NPCLib.getInstance().generateGlobalNPC(NPCBootstrap.getInstance(), npcLocation.getNpcUUID(), new Location(Bukkit.getWorld(npcLocation.getLocationWorld()), npcLocation.getLocationPosX(), npcLocation.getLocationPosY(), npcLocation.getLocationPosZ()));
                    npc.setAutoCreate(true);
                    npc.setAutoShow(true);
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
                                config.names.maintenanceLine2
                                        .replace("&", "§")
                                        .replace("%group_name%", npcLocation.getGroupName())
                                        .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                        .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                        .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()));
                    }else {
                        npc.setText(config.names.defaultLine1
                                        .replace("&", "§")
                                        .replace("%group_name%", npcLocation.getGroupName())
                                        .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                        .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                        .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()),
                                config.names.defaultLine2         .replace("&", "§")   .replace("%group_name%", npcLocation.getGroupName())
                                        .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                        .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                        .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()));
                    }
                    npc.update();
                }else {
                    NPC.Global npc = NPCLib.getInstance().getAllGlobalNPCs().stream().filter(global -> global.getSimpleID().equalsIgnoreCase(npcLocation.getNpcUUID())).findFirst().orElse(null);
                    if (CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).isMaintenance()){
                        if (npc.setText(config.names.maintenanceLine1
                                        .replace("&", "§")
                                        .replace("%group_name%", npcLocation.getGroupName())
                                        .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                        .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                        .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()),
                                config.names.maintenanceLine2
                                        .replace("&", "§").replace("%group_name%", npcLocation.getGroupName())
                                        .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                        .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                        .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size())).hasUpdateNeeded()){
                            npc.setText(config.names.maintenanceLine1
                                            .replace("&", "§")
                                            .replace("%group_name%", npcLocation.getGroupName())
                                            .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                            .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                            .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()),
                                    config.names.maintenanceLine2
                                            .replace("&", "§").replace("%group_name%", npcLocation.getGroupName())
                                            .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                            .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                            .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size())).update();
                        }
                    }else {
                        if (npc.setText(config.names.defaultLine1
                                        .replace("&", "§").replace("%group_name%", npcLocation.getGroupName())
                                        .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                        .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                        .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()),
                                config.names.defaultLine2          .replace("&", "§")  .replace("%group_name%", npcLocation.getGroupName())
                                        .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                        .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                        .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size())).hasUpdateNeeded()){
                            npc.setText(config.names.defaultLine1
                                            .replace("&", "§").replace("%group_name%", npcLocation.getGroupName())
                                            .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                            .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                            .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()),
                                    config.names.defaultLine2          .replace("&", "§")  .replace("%group_name%", npcLocation.getGroupName())
                                            .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                                            .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(npcLocation.getGroupName()).getMaxPlayers())
                                            .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size())).update();
                        }
                    }


                }
            }
        });
    }
}
