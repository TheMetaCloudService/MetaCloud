/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bukkit.events;

import dev.sergiferry.playernpc.api.NPC;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.ItemBuilder;
import eu.metacloudservice.api.ServicePing;
import eu.metacloudservice.bukkit.NPCBootstrap;
import eu.metacloudservice.bukkit.cache.InventoryCache;
import eu.metacloudservice.config.impli.NPCConfig;
import eu.metacloudservice.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.*;

public class Events implements Listener {

    private ArrayList<InventoryCache> caches;

    public Events() {
        caches = new ArrayList<>();
    }

    @EventHandler
    public void onNPCInteract(NPC.Events.Interact event){
        Player player = event.getPlayer();
        NPC npc = event.getNPC();

        String group = NPCBootstrap.getInstance().npcAPI.Locations.getLocations().stream()
                .filter(npcLocation -> npcLocation.getNpcUUID().equalsIgnoreCase(npc.getSimpleID().replace("global_", ""))).findFirst().get().getGroupName();

        NPCConfig config = NPCBootstrap.getInstance().npcAPI.getNPCConfig(group);

        if (CloudAPI.getInstance().getGroupPool().getGroup(group).isMaintenance()){
            event.setCancelled(true);
        }else if (config == null) event.setCancelled(true);
        else {
            if (event.isLeftClick()){
                switch (config.getLeftClickAction()){
                    case CONNECT_RANDOM -> {
                        CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                                .toList().get(new Random().nextInt(CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                                        .toList().size())).connect(player.getName());
                        break;
                    }
                    case CONNECT_LOWEST_PLAYERS ->  {
                        List<Integer> players = new ArrayList<>();
                        CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                                .toList().forEach(cloudService -> {
                                    players.add(cloudService.getPlayers().size());
                                });
                        players.sort(Collections.reverseOrder());
                        CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getPlayercount() == players.get(0)).findFirst().orElse(null).connect(player.getName());
                        break;
                    }
                    case CONNECT_HIGHEST_PLAYERS -> {
                        List<Integer> players = new ArrayList<>();
                        CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                                .toList().forEach(cloudService -> {
                                    players.add(cloudService.getPlayers().size());
                                });
                        players.stream().sorted();
                        CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getPlayercount() == players.get(0)).findFirst().orElse(null).connect(player.getName());
                        break;
                    }
                    case OPEN_INVENTORY -> {
                        openInventory(player, config.targetGroup, config);
                        break;
                    }
                }
            }else if (event.isRightClick()){
                switch (config.getRightClickAction()){
                    case CONNECT_RANDOM -> {
                        CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                                .toList().get(new Random().nextInt(CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                                        .toList().size())).connect(player.getName());
                        break;
                    }
                    case CONNECT_LOWEST_PLAYERS ->  {
                        List<Integer> players = new ArrayList<>();
                        CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                                .toList().forEach(cloudService -> {
                                    players.add(cloudService.getPlayers().size());
                                });
                        players.sort(Collections.reverseOrder());
                        CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getPlayercount() == players.get(0)).findFirst().orElse(null).connect(player.getName());
                        break;
                    }
                    case CONNECT_HIGHEST_PLAYERS -> {
                        List<Integer> players = new ArrayList<>();
                        CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                                .toList().forEach(cloudService -> {
                                    players.add(cloudService.getPlayers().size());
                                });
                        players.stream().sorted();
                        CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getPlayercount() == players.get(0)).findFirst().orElse(null).connect(player.getName());
                        break;
                    }
                    case OPEN_INVENTORY -> {
                        openInventory(player, config.targetGroup, config);
                        break;
                    }
                }
            }
        }

    }

    private void openInventory(Player player, String targetGroup, NPCConfig config){
        Inventory inv = Bukkit.createInventory(player, 9*6, config.inventoryName
                .replace("&", "§")
                .replace("%group_name%", targetGroup)
                .replace("%online_services%", "" + CloudAPI.getInstance().getServicePool().getServicesByGroup(config.targetGroup).stream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).count())
                .replace("%max_players%", "" + CloudAPI.getInstance().getGroupPool().getGroup(targetGroup).getMaxPlayers())
                .replace("%online_players%", ""+CloudAPI.getInstance().getPlayerPool().getPlayersByServiceGroup(config.targetGroup).size()));
        for (int i = 0; i != 9 ; i++)
            inv.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1)
                    .setName(" ").toItemStack());
        for (int i = 45; i != 54 ; i++)
            inv.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1)
                    .setName(" ").toItemStack());
        List<CloudService> services = CloudAPI.getInstance().getServicePool().getServicesByGroup(targetGroup).stream()
                .filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                .sorted(Comparator.comparing(CloudService::getID))
                .toList();
        HashMap<Integer, CloudService> serviceHashMap = new HashMap<>();


        int langs = Math.min(services.size(), 46);

        for (int i = 9; i !=langs+9; i++) {
            CloudService service = services.get(i-9);
            ServicePing ping = new ServicePing();
            try {
                ping.pingServer(service.getAddress(), service.getPort(), 300);
            } catch (IOException ignored) {
            }

            serviceHashMap.put(i, service);
            if (service.getPlayercount() >= service.getGroup().getMaxPlayers()){
                ArrayList<String> lore = config.fullItem.getLore();

                for (int i2 = 0; i2 < lore.size(); i2++) {
                    String originalString = lore.get(i2);
                    String replacedString = originalString
                            .replace("%group_name%", service.getGroup().getGroup())
                            .replace("%service_name%", service.getName())
                            .replace("%max_players%", "" +service.getGroup().getMaxPlayers())
                            .replace("%online_players%", ""+ service.getPlayercount())
                            .replace("%service_motd%", ping.getMotd())
                            .replace("%service_node%", service.getGroup().getStorage().getRunningNode())
                            .replace("%service_id%", service.getID());
                    lore.set(i2, replacedString);
                }
                inv.setItem(i, new ItemBuilder(Material.valueOf(config.fullItem.getMaterial().toUpperCase()), 1)
                        .setName(config.fullItem.displayName
                                .replace("%group_name%", service.getGroup().getGroup())
                                .replace("%service_name%", service.getName())
                                .replace("%max_players%", "" +service.getGroup().getMaxPlayers())
                                .replace("%online_players%", ""+ service.getPlayercount())
                                .replace("%service_motd%", ping.getMotd())
                                .replace("%service_node%", service.getGroup().getStorage().getRunningNode())
                                .replace("%service_id%", service.getID()))
                        .setLore(lore).toItemStack());
            } else if (service.getPlayercount() == 0){
                ArrayList<String> lore = config.emptyItem.getLore();

                for (int i2 = 0; i2 < lore.size(); i2++) {
                    String originalString = lore.get(i2);
                    String replacedString = originalString
                            .replace("%group_name%", service.getGroup().getGroup())
                            .replace("%service_name%", service.getName())
                            .replace("%max_players%", "" +service.getGroup().getMaxPlayers())
                            .replace("%online_players%", ""+ service.getPlayercount())
                            .replace("%service_motd%", ping.getMotd())
                            .replace("%service_node%", service.getGroup().getStorage().getRunningNode())
                            .replace("%service_id%", service.getID());
                    lore.set(i2, replacedString);
                }
                inv.setItem(i, new ItemBuilder(Material.valueOf(config.emptyItem.getMaterial().toUpperCase()), 1)
                        .setName(config.emptyItem.displayName
                                .replace("%group_name%", service.getGroup().getGroup())
                                .replace("%service_name%", service.getName())
                                .replace("%max_players%", "" +service.getGroup().getMaxPlayers())
                                .replace("%online_players%", ""+ service.getPlayercount())
                                .replace("%service_motd%", ping.getMotd())
                                .replace("%service_node%", service.getGroup().getStorage().getRunningNode())
                                .replace("%service_id%", service.getID()))
                        .setLore(lore).toItemStack());
            }
            else{
                ArrayList<String> lore = config.onlineItem.getLore();

                for (int i2 = 0; i2 < lore.size(); i2++) {
                    String originalString = lore.get(i2);
                    String replacedString = originalString
                            .replace("%group_name%", service.getGroup().getGroup())
                            .replace("%service_name%", service.getName())
                            .replace("%max_players%", "" +service.getGroup().getMaxPlayers())
                            .replace("%online_players%", ""+ service.getPlayercount())
                            .replace("%service_motd%", ping.getMotd())
                            .replace("%service_node%", service.getGroup().getStorage().getRunningNode())
                            .replace("%service_id%", service.getID());
                    lore.set(i2, replacedString);
                }
                inv.setItem(i, new ItemBuilder(Material.valueOf(config.onlineItem.getMaterial().toUpperCase()), 1)
                        .setName(config.onlineItem.displayName
                                .replace("%group_name%", service.getGroup().getGroup())
                                .replace("%service_name%", service.getName())
                                .replace("%max_players%", "" +service.getGroup().getMaxPlayers())
                                .replace("%online_players%", ""+ service.getPlayercount())
                                .replace("%service_motd%", ping.getMotd())
                                .replace("%service_node%", service.getGroup().getStorage().getRunningNode())
                                .replace("%service_id%", service.getID()))
                        .setLore(lore).toItemStack());
            }
        }
        player.openInventory(inv);
        caches.add(new InventoryCache(player.getUniqueId(), serviceHashMap));
    }

    @EventHandler
    public void handle(InventoryClickEvent event){
        if (caches.stream().anyMatch(inventoryCache -> inventoryCache.uuid == event.getWhoClicked().getUniqueId())){
            if ( Objects.requireNonNull(caches.stream().filter(inventoryCache -> inventoryCache.uuid == event.getWhoClicked().getUniqueId()).findFirst().orElse(null)).serviceBySlot.containsKey(event.getSlot())){
                Objects.requireNonNull(caches.stream().filter(inventoryCache -> inventoryCache.uuid == event.getWhoClicked().getUniqueId()).findFirst().orElse(null)).serviceBySlot.get(event.getSlot()).connect(event.getWhoClicked().getName());
                caches.removeIf(inventoryCache -> inventoryCache.uuid == event.getWhoClicked().getUniqueId());
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(InventoryCloseEvent event){
        caches.removeIf(inventoryCache -> inventoryCache.uuid == event.getPlayer().getUniqueId());
    }
}
