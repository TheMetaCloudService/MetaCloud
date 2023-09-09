package eu.metacloudservice.bungee.command;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.pool.service.entrys.CloudService;
import lombok.SneakyThrows;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CloudCommand extends Command implements TabExecutor {
    public CloudCommand(String name) {
        super(name);
    }

    @SneakyThrows
    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            Messages messages = CloudAPI.getInstance().getMessages();
            String PREFIX = messages.getPrefix().replace("&", "§");
            if (player.hasPermission("metacloud.command.use")){
                if (args.length == 0){
                    sendHelp(player);
                }else {
                    if (args[0].equalsIgnoreCase("maintenance")){
                        if (args.length == 1){
                            Group groupc = CloudAPI.getInstance().getGroups().parallelStream().filter(group1 -> group1.getGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().get();
                            if (groupc.isMaintenance()) {
                                CloudAPI.getInstance().dispatchCommand("group " + CloudAPI.getInstance().getCurrentService().getGroup() + " setmaintenance false");
                                player.sendMessage(PREFIX + "The network is now no longer in maintenance");
                            } else {

                                CloudAPI.getInstance().dispatchCommand("group " + CloudAPI.getInstance().getCurrentService().getGroup() + " setmaintenance true");
                                player.sendMessage(PREFIX + "The network is now in maintenance");
                            }

                        }else {
                            String group = args[1];
                            if (CloudAPI.getInstance().getGroupsName().contains(group)) {
                                Group groupc = CloudAPI.getInstance().getGroups().parallelStream().filter(group1 -> group1.getGroup().equalsIgnoreCase(group)).findFirst().get();
                                if (groupc.isMaintenance()) {
                                    CloudAPI.getInstance().dispatchCommand("group " + group + " setmaintenance false");
                                    player.sendMessage(PREFIX + "The group '§f"+group+"§7' is now no longer in maintenance");
                                } else {

                                    CloudAPI.getInstance().dispatchCommand("group " + group + " setmaintenance true");
                                    player.sendMessage(PREFIX + "The group '§f"+group+"§7' is now in maintenance");
                                }
                            }
                        }

                    }else if (args[0].equalsIgnoreCase("maxplayers")){
                        if (args.length == 2){
                            int amount = Integer.parseInt(args[1]);

                            CloudAPI.getInstance().dispatchCommand("group " + CloudAPI.getInstance().getCurrentService().getGroup() + " setmaxplayers " + amount);
                            player.sendMessage(PREFIX + "The number of players has been adjusted");


                        }else if (args.length == 3){
                            int amount = Integer.parseInt(args[1]);
                            String group = args[2];
                            if (CloudAPI.getInstance().getGroupsName().contains(group)){

                                CloudAPI.getInstance().dispatchCommand("group " + group + " setmaxplayers " + amount);
                                player.sendMessage(PREFIX + "The number of players has been adjusted");

                            }else {
                                player.sendMessage(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.");
                            }


                        }else {
                            sendHelp(player);
                        }
                    }else if (args[0].equalsIgnoreCase("stop")){
                        if (args.length >= 2){
                            String group = args[1];
                            if (CloudAPI.getInstance().getServicePool().getService(group) != null){
                                if (args.length == 2){
                                    CloudAPI.getInstance().dispatchCommand("service stop " + group );
                                    player.sendMessage(PREFIX + "The '§f"+group+"§7' service is now stopped");
                                }else {
                                    sendHelp(player);
                                }
                            }else {
                                player.sendMessage(PREFIX + "The service you are looking for was not found, please check that it is spelled correctly.");
                            }
                        }else {
                            sendHelp(player);
                        }

                    }else if (args[0].equalsIgnoreCase("stopgroup")){
                        if (args.length >= 2){
                            String group = args[1];
                            if (CloudAPI.getInstance().getGroupsName().contains(group)){
                                if (args.length == 2){
                                    CloudAPI.getInstance().dispatchCommand("service stopgroup " + group );
                                    player.sendMessage(PREFIX + "The '§f"+group+"§7' group is now stopped");
                                }else {
                                    sendHelp(player);
                                }
                            }else {
                                player.sendMessage(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.");
                            }
                        }else {
                            sendHelp(player);
                        }
                    }else if (args[0].equalsIgnoreCase("run")){
                        if (args.length == 2){
                            String group = args[1];

                            if (CloudAPI.getInstance().getGroupsName().contains(group)){

                                CloudAPI.getInstance().dispatchCommand("service run " + group + " "+  1);
                                player.sendMessage(PREFIX + "services have been started from the group you have selected");

                            }else {
                                player.sendMessage(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.");
                            }


                        }else  if (args.length == 3) {
                            String group = args[1];
                            int amount = Integer.parseInt(args[2]);

                            if (CloudAPI.getInstance().getGroupsName().contains(group)){

                                CloudAPI.getInstance().dispatchCommand("service run " + group + " "+  amount);
                                player.sendMessage(PREFIX + "services have been started from the group you have selected");

                            }else {
                                player.sendMessage(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.");
                            }

                        }else {
                                sendHelp(player);
                        }
                    }else if (args[0].equalsIgnoreCase("whitelist")){
                        List<String> list = CloudAPI.getInstance().getWhitelist();
                        if (args.length == 1){
                            if (list.isEmpty()){
                                player.sendMessage(PREFIX + "No players were found on the whitelist");
                            }

                            list.forEach(s -> {
                                player.sendMessage(PREFIX + s);
                            });
                        }else if (args.length == 2){
                            String name = args[2];
                            if (args[1].equalsIgnoreCase("add")){
                                if (list.contains(name)){
                                    player.sendMessage(PREFIX +  "The player  '§f"+name+"§7'  is now on the whitelist");
                                    CloudAPI.getInstance().addWhiteList(name);
                                }else {
                                    player.sendMessage(PREFIX +  "The player  '§f"+name+"§7'  is already whitelisted");
                                }
                            }else if (args[1].equalsIgnoreCase("remove")){
                                if (!list.contains(name)){
                                    CloudAPI.getInstance().removeWhiteList(name);
                                    player.sendMessage(PREFIX +  "The player  '§f"+name+"§7'  is now no longer on the whitelist");
                                }else {
                                    player.sendMessage(PREFIX +  "The player '§f"+name+"§7' is not whitelisted");
                                }
                            }else {
                                sendHelp(player);
                            }
                        }else {
                            sendHelp(player);
                        }

                    }else if (args[0].equalsIgnoreCase("dispatch")){
                        if (args.length >= 3){
                            StringBuilder msg = new StringBuilder();
                            String service = args[1];
                            for (int i = 2; i < args.length; i++) {
                                msg.append(args[i]).append(" ");
                            }
                            if (CloudAPI.getInstance().getServicePool().getServices().stream().anyMatch(service1 -> service1.getName().equalsIgnoreCase(service))){
                                player.sendMessage(PREFIX + "The command '§f"+msg.toString()+"§7' was sent to the service '§f"+service+"§7'");
                                CloudAPI.getInstance().getServicePool().getService(service).dispatchCommand(msg.toString());
                            }else {
                                player.sendMessage(PREFIX + "The service you are looking for was not found, please check that it is spelled correctly.");
                            }

                        }else {
                            sendHelp(player);
                        }

                    }else if (args[0].equalsIgnoreCase("list")){
                        if (args.length == 2){
                            String chose = args[1];
                            if (chose.equalsIgnoreCase("players")){
                                List<CloudPlayer> players = CloudAPI.getInstance().getPlayerPool().getPlayers();
                                if (players.isEmpty()){

                                }else {
                                    String playerss = players.get(0).getUsername();
                                    if (players.size() != 1){
                                        for (int i = 1; i != players.size(); i++){
                                            playerss = playerss + "§7, §f" + players.get(i).getUsername();
                                        }
                                    }
                                    player.sendMessage(PREFIX + "current players §8⯮ §f" + playerss);
                                }
                            }else if (chose.equalsIgnoreCase("services")){
                                CloudAPI.getInstance().getServicePool().getServices().forEach(service -> {
                                    player.sendMessage(PREFIX + "§f" + service.getName() +
                                            " §7| GROUP §8⯮ §f" + service.getGroup() +
                                            "§7 - PLAYERS §8⯮ §f" + service.getPlayers().size() +
                                            "§7 ~ STATE §8⯮ §f" + service.getState());
                                });
                            }else {
                                sendHelp(player);
                            }
                        }else {
                            sendHelp(player);
                        }

                    }else if (args[0].equalsIgnoreCase("sync")){
                        if (args.length == 2){
                            String service = args[1];
                            if (CloudAPI.getInstance().getServicePool().serviceNotNull(service)){
                                AsyncCloudAPI.getInstance().getServicePool().getService(service).get().sync();
                                player.sendMessage(PREFIX + "The service '§f"+service+"§7' was successfully synchronized");
                            }else {
                                player.sendMessage(PREFIX + "The service was not found and therefore cannot be synced");
                            }
                        }else {
                            sendHelp(player);
                        }
                    }else if (args[0].equalsIgnoreCase("exit")){
                        AsyncCloudAPI.getInstance().dispatchCommand("shutdown");
                        AsyncCloudAPI.getInstance().dispatchCommand("shutdown");
                    }else if (args[0].equalsIgnoreCase("version")){
                        player.sendMessage(PREFIX + "The cloud is currently running on version §8⯮ §f" + Driver.getInstance().getMessageStorage().version);

                    }else if (args[0].equalsIgnoreCase("reload")){
                        AsyncCloudAPI.getInstance().dispatchCommand("reload all");
                        player.sendMessage(PREFIX + "The network will now be reloaded");
                    }else {
                        sendHelp(player);
                    }
                }
            }else {
                player.sendMessage("§8▷ §7The network uses §bMetacloud§8 [§a"+Driver.getInstance().getMessageStorage().version+"§8]");
                player.sendMessage("§8▷ §fhttps://metacloudservice.eu/");
            }
        }
    }


    public void sendHelp(ProxiedPlayer player){
        Messages messages = CloudAPI.getInstance().getMessages();
        String PREFIX = messages.getPrefix().replace("&", "§");
        player.sendMessage( PREFIX + "/cloud maintenance (group)");
        player.sendMessage( PREFIX + "/cloud maxplayers <amount> (group)");
        player.sendMessage( PREFIX + "/cloud run <group> (amount)");
        player.sendMessage( PREFIX + "/cloud stop <service>");
        player.sendMessage( PREFIX + "/cloud stopgroup <group>");
        player.sendMessage( PREFIX + "/cloud whitelist (<add/remove>) (<player>)");
        player.sendMessage( PREFIX + "/cloud dispatch <service> <command>");
        player.sendMessage( PREFIX + "/cloud list <players/services>");
        player.sendMessage( PREFIX + "/cloud sync <service>");
        player.sendMessage( PREFIX + "/cloud exit");
        player.sendMessage( PREFIX + "/cloud version");
        player.sendMessage( PREFIX + "/cloud reload");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            // List of main command arguments
            suggestions.add("maintenance");
            suggestions.add("maxplayers");
            suggestions.add("stop");
            suggestions.add("stopgroup");
            suggestions.add("run");
            suggestions.add("whitelist");
            suggestions.add("dispatch");
            suggestions.add("list");
            suggestions.add("sync");
            suggestions.add("exit");
            suggestions.add("version");
            suggestions.add("reload");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("sync") || args[0].equalsIgnoreCase("dispatch")
                    || args[0].equalsIgnoreCase("stop")){
                CloudAPI.getInstance().getServicePool().getServices().forEach(cloudService -> {
                    suggestions.add(cloudService.getName());
                });
            }else  if (args[0].equalsIgnoreCase("list")){
                CloudAPI.getInstance().getServicePool().getServices().forEach(cloudService -> {
                    suggestions.add(cloudService.getName());
                });

                CloudAPI.getInstance().getPlayerPool().getPlayers().forEach(cloudService -> {
                    suggestions.add(cloudService.getUsername());
                });
            }
            else if (args[0].equalsIgnoreCase("stopgroup") || args[0].equalsIgnoreCase("maintenance")|| args[0].equalsIgnoreCase("run")){
                CloudAPI.getInstance().getGroups().forEach(group -> {
                    suggestions.add(group.getGroup());
                });
            }else if (args[0].equalsIgnoreCase("whitelist")){
                suggestions.add("add");
                suggestions.add("remove");
            }
        }else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("whitelist")){
                CloudAPI.getInstance().getPlayerPool().getPlayers().forEach(cloudService -> {
                    suggestions.add(cloudService.getUsername());
                });
            }else   if (args[0].equalsIgnoreCase("maxplayers")){
                CloudAPI.getInstance().getGroups().forEach(group -> {
                    suggestions.add(group.getGroup());
                });
            }
        }
        // Filter suggestions based on the current input
        String prefix = args[args.length - 1].toLowerCase();
        return suggestions.stream()
                .filter(suggestion -> suggestion.toLowerCase().startsWith(prefix))
                .collect(Collectors.toList());
    }
}
