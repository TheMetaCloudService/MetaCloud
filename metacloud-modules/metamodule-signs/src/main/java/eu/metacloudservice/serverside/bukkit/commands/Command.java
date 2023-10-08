/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.commands;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.api.PluginCommand;
import eu.metacloudservice.api.PluginCommandInfo;
import eu.metacloudservice.config.Locations;
import eu.metacloudservice.config.SignLocation;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.serverside.bukkit.SignBootstrap;
import eu.metacloudservice.serverside.bukkit.entry.CloudSign;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@PluginCommandInfo(command = "sign", description = "/service sign")
public class Command extends PluginCommand {

    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, com.velocitypowered.api.proxy.Player veloPlayer, Player player, String[] args) {
        String prefix = CloudAPI.getInstance().getMessages().getPrefix().replace("&", "§");
        if (args.length == 0){
            sendHelp(player);

        }else if (args[0].equalsIgnoreCase("create")){
            if (args.length == 2){
                Block targetBlock = player.getTargetBlock(null, 4);

                if (targetBlock.getState() instanceof Sign){
                    Group targetGroup  = CloudAPI.getInstance().getGroupPool().getGroup(args[1]);
                    if (targetGroup != null){
                        if (!targetGroup.getGroupType().equalsIgnoreCase("PROXY")){
                            if (!SignBootstrap.signsAPI.canFind(targetBlock.getLocation())){
                                UUID uuid = UUID.randomUUID();
                                SignBootstrap.signDriver.addSign(uuid, targetGroup.getGroup(), targetBlock.getLocation());
                                SignBootstrap.signsAPI.createSign(new SignLocation(uuid.toString(), targetBlock.getLocation().getX(), targetBlock.getLocation().getY(), targetBlock.getLocation().getZ(), targetBlock.getLocation().getWorld().getName(), targetGroup.getGroup()));
                                player.sendMessage(prefix + "The sign was added.");
                            }else {
                                player.sendMessage(prefix + "There is already a sign registered on this location.");

                            }
                        }else {
                            player.sendMessage(prefix + "Cannot create a sign for proxy groups.");

                        }
                    }else {
                        player.sendMessage(prefix + "The group not found.");

                    }
                }else {
                    player.sendMessage(prefix + "You must look at a sign.");
                }
            }else {
                sendHelp(player);
            }
        }else if (args[0].equalsIgnoreCase("delete")){
            Block targetBlock = player.getTargetBlock(null, 4);
            if (!SignBootstrap.signsAPI.canFind(targetBlock.getLocation())){
                player.sendMessage(prefix + "The sign is not found");
            }else {
                UUID uuid = SignBootstrap.signDriver.getUUIDByLocation(targetBlock.getLocation());
                SignBootstrap.signDriver.removeSign(uuid);
                SignBootstrap.signsAPI.removeSign(uuid.toString());
                player.sendMessage(prefix + "The sign was removed");
            }

        }
    }

    public void sendHelp(Player player){
        String prefix = CloudAPI.getInstance().getMessages().getPrefix().replace("&", "§");
        player.sendMessage(prefix + "/service sign create [group]");
        player.sendMessage(prefix + "/service sign delete");


    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 0) {
            List<String> completions = new ArrayList<>();
            completions.add("create");
            completions.add("delete");
            return completions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            return CloudAPI.getInstance().getGroupPool().getGroups().stream().filter(group -> !group.getGroupType().equalsIgnoreCase("PROXY")).map(Group::getGroup).filter(group -> group.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());

        }
        return new ArrayList<>();
    }
}
