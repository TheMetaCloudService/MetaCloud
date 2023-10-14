/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bukkit.commands;

import dev.sergiferry.playernpc.api.NPCLib;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.PluginCommand;
import eu.metacloudservice.api.PluginCommandInfo;
import eu.metacloudservice.bukkit.NPCBootstrap;
import eu.metacloudservice.config.enums.ClickAction;
import eu.metacloudservice.config.impli.NPCLocation;
import eu.metacloudservice.groups.dummy.Group;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@PluginCommandInfo(command = "npc" , description = "/service npc")
public class NPCCommand extends PluginCommand {
    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, com.velocitypowered.api.proxy.Player veloPlayer, Player player, String[] args) {
        String prefix = CloudAPI.getInstance().getMessages().getMessages().get("prefix").replace("&", "§");
        if (args.length == 0){
            sendHelp(player);
        }else if (args[0].equalsIgnoreCase("create") && args.length == 3){
            String group = args[1];
            String name = args[2];
            if (CloudAPI.getInstance().getGroupPool().isGroupExists(group)){
                Group targetGroup  = CloudAPI.getInstance().getGroupPool().getGroup(args[1]);
                if (!targetGroup.getGroupType().equalsIgnoreCase("PROXY")){
                    NPCBootstrap.getInstance().npcAPI.createNPC(new NPCLocation(UUID.randomUUID().toString(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
                            Objects.requireNonNull(player.getLocation().getWorld()).getName(), group, null, null, name.equals("SHOW_OWN_SKIN") ? "" : name, name.equals("SHOW_OWN_SKIN")));
                }else {
                    player.sendMessage(prefix + "Cannot create a sign for proxy groups.");
                }
            }else {
                player.sendMessage(prefix +  "The group you are looking for was not found, please check that it is spelled correctly.");
            }
        }else if (args[0].equalsIgnoreCase("delete") && args.length == 2){
            String id = args[1];
            if (NPCBootstrap.getInstance().npcAPI.Locations.getLocations().stream().anyMatch(npcLocation -> npcLocation.getNpcUUID().equalsIgnoreCase(id))){
                NPCBootstrap.getInstance().npcAPI.removeNPC(id);
                NPCLib.getInstance().removeGlobalNPC(Objects.requireNonNull(NPCLib.getInstance().getGlobalNPC(NPCBootstrap.getInstance(), id)));
            }else {
                player.sendMessage(prefix + "the id dos not exist, pleas try it again with an correct one");
            }
        }else {
            sendHelp(player);
        }
    }

    private void sendHelp(Player player){
        String prefix = CloudAPI.getInstance().getMessages().getMessages().get("prefix").replace("&", "§");
        player.sendMessage(prefix + "/service npc create [group] [name or SHOW_OWN_SKIN]");
        player.sendMessage(prefix + "/service npc delete [id]");
    }

    @Override
    public List<String> tabComplete(String[] args) {
        ArrayList<String> suggestions = new ArrayList<>();
        if (args.length == 0){
            suggestions.add("create");
            suggestions.add("delete");
        }else if (args[0].equalsIgnoreCase("delete") && args.length == 2){
            NPCBootstrap.getInstance().npcAPI.Locations.getLocations().forEach(npcLocation -> suggestions.add(npcLocation.getNpcUUID()));
        }else if (args[0].equalsIgnoreCase("create") && args.length == 2){
            CloudAPI.getInstance().getGroupPool().getGroups().stream().filter(group -> !group.getGroupType().equalsIgnoreCase("PROXY")).forEach(group -> suggestions.add(group.getGroup()));
        }else if (args[0].equalsIgnoreCase("create") && args.length == 3){
            suggestions.add("SHOW_OWN_SKIN");
            suggestions.add("[player name]");
          }
        return suggestions;
    }
}
