/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.subcommands;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.PluginCommand;
import eu.metacloudservice.api.PluginCommandInfo;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.in.service.command.PacketInCommandMaintenance;
import eu.metacloudservice.networking.in.service.command.PacketInCommandMaxPlayers;
import eu.metacloudservice.networking.in.service.command.PacketInCommandMinCount;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

@PluginCommandInfo(command = "group", description = "/cloud group")
public class GroupCommand extends PluginCommand {
    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, org.bukkit.entity.Player bukkitPlayer, String[] args) {
        Messages messages = CloudAPI.getInstance().getMessages();
        String PREFIX = messages.getPrefix().replace("&", "§");
        if (args.length == 0){
            if (proxiedPlayer == null) {
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group list"));
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group maintenance ([group])"));
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group maxplayers [group] [amount]"));
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group mincount [group] [amount]"));
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group dispatch [group] [command]"));
            } else {
                proxiedPlayer.sendMessage(PREFIX + "/cloud group list");
                proxiedPlayer.sendMessage(PREFIX + "/cloud group maintenance ([group])");
                proxiedPlayer.sendMessage(PREFIX + "/cloud group maxplayers [group] [amount]");
                proxiedPlayer.sendMessage(PREFIX + "/cloud group mincount [group] [amount]");
                proxiedPlayer.sendMessage(PREFIX + "/cloud group dispatch [group] [command]");
            }
        }else if (args[0].equalsIgnoreCase("list")){
            CloudAPI.getInstance().getGroupPool().getGroupsByName().forEach(s -> {
                if (proxiedPlayer == null) {
                    veloPlayer.sendMessage(Component.text(PREFIX + s));
                } else {
                    proxiedPlayer.sendMessage(PREFIX + s);
                }
            });
        }else if (args[0].equalsIgnoreCase("maintenance")){
            if (args.length == 1){
                Group groupc = CloudAPI.getInstance().getGroupPool().getGroups().parallelStream().filter(group1 -> group1.getGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().get();
                if (groupc.isMaintenance()) {
                    CloudAPI.getInstance().sendPacketSynchronized(new PacketInCommandMaintenance( CloudAPI.getInstance().getCurrentService().getGroup(), false));
                    if (veloPlayer != null)
                        veloPlayer.sendMessage(Component.text(PREFIX +"The '§fnetwork§7' is no longer in maintenance"));
                    else
                        proxiedPlayer.sendMessage(PREFIX + "The '§fnetwork§7' is no longer in maintenance");
                } else {

                    CloudAPI.getInstance().sendPacketSynchronized(new PacketInCommandMaintenance( CloudAPI.getInstance().getCurrentService().getGroup(), true));
                    if (veloPlayer != null)
                        veloPlayer.sendMessage(Component.text(PREFIX +"The '§fnetwork§7' is now in maintenance"));
                    else
                        proxiedPlayer.sendMessage(PREFIX + "The '§fnetwork§7' is now in maintenance");

                }

            }else {
                String group = args[1];
                if ( CloudAPI.getInstance().getGroupPool().getGroupsByName().contains(group)) {
                    Group groupc = CloudAPI.getInstance().getGroupPool().getGroups().parallelStream().filter(group1 -> group1.getGroup().equalsIgnoreCase(group)).findFirst().get();
                    if (groupc.isMaintenance()) {
                        CloudAPI.getInstance().sendPacketSynchronized(new PacketInCommandMaintenance( group, false));
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX +  "The group '§f"+group+"§7' is no longer in maintenance"));
                        else
                            proxiedPlayer.sendMessage(PREFIX +   "The group '§f"+group+"§7' is no longer in maintenance");
                    } else {

                        CloudAPI.getInstance().sendPacketSynchronized(new PacketInCommandMaintenance( group, true));
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX +  "The group '§f"+group+"§7' is now in maintenance"));
                        else
                            proxiedPlayer.sendMessage(PREFIX +   "The group '§f"+group+"§7' is now in maintenance");
                    }
                }else {
                    if (veloPlayer != null)
                        veloPlayer.sendMessage(Component.text(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly."));
                    else
                        proxiedPlayer.sendMessage(PREFIX +  "The group you are looking for was not found, please check that it is spelled correctly.");
                }
            }
        }else if (args[0].equalsIgnoreCase("maxplayers")){
                if (args.length == 3){
                int amount = Integer.parseInt(args[2]);
                String group = args[1];
                if (CloudAPI.getInstance().getGroupPool().getGroupsByName().contains(group)){

                    CloudAPI.getInstance().sendPacketSynchronized(new PacketInCommandMaxPlayers( group, amount));
                    if (veloPlayer != null)
                        veloPlayer.sendMessage(Component.text(PREFIX + "The player count have been adjusted to '§f"+amount+"§7' for the '§f"+group+"§7' group."));
                    else
                        proxiedPlayer.sendMessage(PREFIX + "The player count have been adjusted to '§f"+amount+"§7' for the '§f"+group+"§7' group.");

                }else {
                    if (veloPlayer != null)
                        veloPlayer.sendMessage(Component.text(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly."));
                    else
                        proxiedPlayer.sendMessage(PREFIX +  "The group you are looking for was not found, please check that it is spelled correctly.");
                }


            }else {
                    if (proxiedPlayer == null) {
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group list"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group maintenance ([group])"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group maxplayers [group] [amount]"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group mincount [group] [amount]"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group dispatch [group] [command]"));
                    } else {
                        proxiedPlayer.sendMessage(PREFIX + "/cloud group list");
                        proxiedPlayer.sendMessage(PREFIX + "/cloud group maintenance ([group])");
                        proxiedPlayer.sendMessage(PREFIX + "/cloud group maxplayers [group] [amount]");
                        proxiedPlayer.sendMessage(PREFIX + "/cloud group mincount [group] [amount]");
                        proxiedPlayer.sendMessage(PREFIX + "/cloud group dispatch [group] [command]");
                    }
            }
        }else if (args[0].equalsIgnoreCase("mincount")){

            if (args.length == 3){
                int amount = Integer.parseInt(args[2]);
                String group = args[1];
                if (CloudAPI.getInstance().getGroupPool().getGroupsByName().contains(group)){

                    CloudAPI.getInstance().sendPacketSynchronized(new PacketInCommandMinCount( group, amount));
                    if (veloPlayer != null)
                        veloPlayer.sendMessage(Component.text(PREFIX + "The minimum amount of the group '§f"+group+"§7' adjusted to '§f"+amount+"§7'"));
                    else
                        proxiedPlayer.sendMessage(PREFIX + "The minimum amount of the group '§f"+group+"§7' adjusted to '§f"+amount+"§7'");

                }else {
                    if (veloPlayer != null)
                        veloPlayer.sendMessage(Component.text(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly."));
                    else
                        proxiedPlayer.sendMessage(PREFIX +  "The group you are looking for was not found, please check that it is spelled correctly.");
                }


            }else {
                if (proxiedPlayer == null) {
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group list"));
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group maintenance ([group])"));
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group maxplayers [group] [amount]"));
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group mincount [group] [amount]"));
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group dispatch [group] [command]"));
                } else {
                    proxiedPlayer.sendMessage(PREFIX + "/cloud group list");
                    proxiedPlayer.sendMessage(PREFIX + "/cloud group maintenance ([group])");
                    proxiedPlayer.sendMessage(PREFIX + "/cloud group maxplayers [group] [amount]");
                    proxiedPlayer.sendMessage(PREFIX + "/cloud group mincount [group] [amount]");
                    proxiedPlayer.sendMessage(PREFIX + "/cloud group dispatch [group] [command]");
                }
            }
        }else if (args[0].equalsIgnoreCase("dispatch")){
            if (args.length >= 3){
                StringBuilder msg = new StringBuilder();
                String service = args[1];
                for (int i = 2; i < args.length; i++) {
                    if (i == args.length-1){
                        msg.append(args[i]);
                    }else {
                        msg.append(args[i]).append(" ");
                    }
                }
                if (CloudAPI.getInstance().getGroupPool().getGroups().stream().anyMatch(group -> group.getGroup().equalsIgnoreCase(service))){

                    if (veloPlayer != null)
                        veloPlayer.sendMessage(Component.text(PREFIX + "The command '§f"+msg.toString()+"§7' was sent to the group '§f"+service+"§7'"));
                    else
                        proxiedPlayer.sendMessage(PREFIX +   "The command '§f"+msg.toString()+"§7' was sent to the group '§f"+service+"§7'");
                    CloudAPI.getInstance().getServicePool().getServicesByGroup(service).forEach(cloudService -> cloudService.dispatchCommand(msg.toString()));
                }else {
                    if (veloPlayer != null)
                        veloPlayer.sendMessage(Component.text(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly."));
                    else
                        proxiedPlayer.sendMessage(PREFIX +  "The group you are looking for was not found, please check that it is spelled correctly.");
                }
            }else {
                if (proxiedPlayer == null) {
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group list"));
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group maintenance ([group])"));
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group maxplayers [group] [amount]"));
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group mincount [group] [amount]"));
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group dispatch [group] [command]"));
                } else {
                    proxiedPlayer.sendMessage(PREFIX + "/cloud group list");
                    proxiedPlayer.sendMessage(PREFIX + "/cloud group maintenance ([group])");
                    proxiedPlayer.sendMessage(PREFIX + "/cloud group maxplayers [group] [amount]");
                    proxiedPlayer.sendMessage(PREFIX + "/cloud group mincount [group] [amount]");
                    proxiedPlayer.sendMessage(PREFIX + "/cloud group dispatch [group] [command]");
                }
            }
        }else {
            if (proxiedPlayer == null) {
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group list"));
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group maintenance ([group])"));
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group maxplayers [group] [amount]"));
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group mincount [group] [amount]"));
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud group dispatch [group] [command]"));
            } else {
                proxiedPlayer.sendMessage(PREFIX + "/cloud group list");
                proxiedPlayer.sendMessage(PREFIX + "/cloud group maintenance ([group])");
                proxiedPlayer.sendMessage(PREFIX + "/cloud group maxplayers [group] [amount]");
                proxiedPlayer.sendMessage(PREFIX + "/cloud group mincount [group] [amount]");
                proxiedPlayer.sendMessage(PREFIX + "/cloud group dispatch [group] [command]");
            }
        }
    }



    @Override
    public List<String> tabComplete(String[] args) {
        List<String > suggestion =  new ArrayList<>();
        if (args.length == 0){
            suggestion.add("list");
            suggestion.add("maintenance");
            suggestion.add("maxplayers");
            suggestion.add("mincount");
            suggestion.add("dispatch");
        }else if (args.length == 2){
            if (!args[0].equalsIgnoreCase("list"))
             suggestion.addAll(CloudAPI.getInstance().getGroupPool().getGroupsByName());
        }
        return suggestion;
    }
}
