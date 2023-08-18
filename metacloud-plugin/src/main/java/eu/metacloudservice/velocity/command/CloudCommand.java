package eu.metacloudservice.velocity.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.pool.service.entrys.CloudService;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CloudCommand implements SimpleCommand {


 


    @SneakyThrows
    @Override
    public void execute(Invocation invocation) {
       String[] args = invocation.arguments();
        if (invocation.source() instanceof  Player){
            Player player = (Player) invocation.source();
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
                                player.sendMessage(Component.text(PREFIX + "The network is now no longer in maintenance"));
                            } else {

                                CloudAPI.getInstance().dispatchCommand("group " + CloudAPI.getInstance().getCurrentService().getGroup() + " setmaintenance true");
                                player.sendMessage(Component.text(PREFIX + "The network is now in maintenance"));
                            }

                        }else {
                            String group = args[1];
                            if (CloudAPI.getInstance().getGroupsName().contains(group)) {
                                Group groupc = CloudAPI.getInstance().getGroups().parallelStream().filter(group1 -> group1.getGroup().equalsIgnoreCase(group)).findFirst().get();
                                if (groupc.isMaintenance()) {
                                    CloudAPI.getInstance().dispatchCommand("group " + group + " setmaintenance false");
                                    player.sendMessage(Component.text(PREFIX + "The group '§f"+group+"§7' is now no longer in maintenance"));
                                } else {

                                    CloudAPI.getInstance().dispatchCommand("group " + group + " setmaintenance true");
                                    player.sendMessage(Component.text(PREFIX + "The group '§f"+group+"§7' is now in maintenance"));
                                }
                            }
                        }


                    }else if (args[0].equalsIgnoreCase("maxplayers")){
                        if (args.length == 2){
                            int amount = Integer.parseInt(args[1]);

                            CloudAPI.getInstance().dispatchCommand("group " + CloudAPI.getInstance().getCurrentService().getGroup() + " setmaxplayers " + amount);
                            player.sendMessage(Component.text(PREFIX + "The number of players has been adjusted"));


                        }else if (args.length == 3){
                            int amount = Integer.parseInt(args[1]);
                            String group = args[2];
                            if (CloudAPI.getInstance().getGroupsName().contains(group)){

                                CloudAPI.getInstance().dispatchCommand("group " + group + " setmaxplayers " + amount);
                                player.sendMessage(Component.text(PREFIX + "The number of players has been adjusted"));

                            }else {
                                player.sendMessage(Component.text(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly."));
                            }


                        }else {
                            sendHelp(player);
                        }
                    }else if (args[0].equalsIgnoreCase("stop")){
                        if (args.length == 2){
                            String group = args[1];
                            if (CloudAPI.getInstance().getServicePool().getService(group) != null){
                                if (args.length == 2){
                                    CloudAPI.getInstance().dispatchCommand("service stop " + group );
                                    player.sendMessage(Component.text(PREFIX + "The '§f"+group+"§7' service is now stopped"));
                                }else {
                                    sendHelp(player);
                                }
                            }else {
                                player.sendMessage(Component.text(PREFIX + "The service you are looking for was not found, please check that it is spelled correctly."));
                            }
                        }else {
                            sendHelp(player);
                        }
                    }else if (args[0].equalsIgnoreCase("stopgroup")){
                        if (args.length == 2){
                            String group = args[1];
                            if (CloudAPI.getInstance().getGroupsName().contains(group)){
                                if (args.length == 2){
                                    CloudAPI.getInstance().dispatchCommand("service stopgroup " + group );
                                    player.sendMessage(Component.text(PREFIX + "The '§f"+group+"§7' group is now stopped"));
                                }else {
                                    sendHelp(player);
                                }
                            }else {
                                player.sendMessage(Component.text(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly."));
                            }
                        }else {
                            sendHelp(player);
                        }
                    }else if (args[0].equalsIgnoreCase("run")){
                        if (args.length == 2){
                            String group = args[1];

                            if (CloudAPI.getInstance().getGroupsName().contains(group)){

                                CloudAPI.getInstance().dispatchCommand("service run " + group + " "+  1);
                                player.sendMessage(Component.text(PREFIX + "services have been started from the group you have selected"));

                            }else {
                                player.sendMessage(Component.text(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly."));
                            }


                        }else  if (args.length == 3) {
                            String group = args[1];
                            int amount = Integer.parseInt(args[2]);

                            if (CloudAPI.getInstance().getGroupsName().contains(group)){

                                CloudAPI.getInstance().dispatchCommand("service run " + group + " "+  amount);
                                player.sendMessage(Component.text(PREFIX + "services have been started from the group you have selected"));

                            }else {
                                player.sendMessage(Component.text(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly."));
                            }

                        }else {
                            sendHelp(player);
                        }
                    }else if (args[0].equalsIgnoreCase("whitelist")){
                        List<String> list = CloudAPI.getInstance().getWhitelist();
                        if (args.length == 1){
                            if (list.isEmpty()){
                                player.sendMessage(Component.text(PREFIX + "No players were found on the whitelist"));
                            }

                            list.forEach(s -> {
                                player.sendMessage(Component.text(PREFIX + s));
                            });
                        }else if (args.length == 2){
                            String name = args[2];
                            if (args[1].equalsIgnoreCase("add")){
                                if (list.contains(name)){
                                    player.sendMessage(Component.text(PREFIX +  "The player  '§f"+name+"§7'  is now on the whitelist"));
                                    CloudAPI.getInstance().addWhiteList(name);
                                }else {
                                    player.sendMessage(Component.text(PREFIX +  "The player  '§f"+name+"§7'  is already whitelisted"));
                                }
                            }else if (args[1].equalsIgnoreCase("remove")){
                                if (!list.contains(name)){
                                    CloudAPI.getInstance().removeWhiteList(name);
                                    player.sendMessage(Component.text(PREFIX +  "The player  '§f"+name+"§7'  is now no longer on the whitelist"));
                                }else {
                                    player.sendMessage(Component.text(PREFIX +  "The player '§f"+name+"§7' is not whitelisted"));
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
                                player.sendMessage(Component.text(PREFIX + "The command '§f"+msg.toString()+"§7' was sent to the service '§f"+service+"§7'"));
                                CloudAPI.getInstance().getServicePool().getService(service).dispatchCommand(msg.toString());
                            }else {
                                player.sendMessage(Component.text(PREFIX + "The service you are looking for was not found, please check that it is spelled correctly."));
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
                                    player.sendMessage(Component.text(PREFIX + "current players §8⯮ §f" + playerss));
                                }
                            }else if (chose.equalsIgnoreCase("services")){
                                CloudAPI.getInstance().getServicePool().getServices().forEach(service -> {
                                    player.sendMessage(Component.text(PREFIX + "§f" + service.getName() +
                                            " §7| GROUP §8⯮ §f" + service.getGroup() +
                                            "§7 - PLAYERS §8⯮ §f" + service.getPlayers().size() +
                                            "§7 ~ STATE §8⯮ §f" + service.getState()));
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
                                player.sendMessage(Component.text(PREFIX + "The service '§f"+service+"§7' was successfully synchronized"));
                            }else {
                                player.sendMessage(Component.text(PREFIX + "The service was not found and therefore cannot be synced"));
                            }
                        }else {
                            sendHelp(player);
                        }
                    }else if (args[0].equalsIgnoreCase("exit")){
                        AsyncCloudAPI.getInstance().dispatchCommand("shutdown");
                        AsyncCloudAPI.getInstance().dispatchCommand("shutdown");
                    }else if (args[0].equalsIgnoreCase("version")){
                        player.sendMessage(Component.text(PREFIX + "The cloud is currently running on version §8⯮ §f" + Driver.getInstance().getMessageStorage().version));
                    }else if (args[0].equalsIgnoreCase("reload")){
                        AsyncCloudAPI.getInstance().dispatchCommand("reload");
                        player.sendMessage(Component.text(PREFIX + "The network will now be reloaded"));
                    }else {
                        sendHelp(player);
                    }
                }
            }else {
                player.sendMessage(Component.text("§8▷ §7The network uses §bMetacloud§8 [§a"+Driver.getInstance().getMessageStorage().version+"§8]"));
                player.sendMessage(Component.text("§8▷ §fhttps://metacloudservice.eu/"));
            }
        }
    }

    public void sendHelp(Player player){
        Messages messages = CloudAPI.getInstance().getMessages();
        String PREFIX = messages.getPrefix().replace("&", "§");
        player.sendMessage(Component.text(PREFIX + "/cloud maintenance (group)"));
        player.sendMessage(Component.text(PREFIX + "/cloud maxplayers <amount> (group)"));
        player.sendMessage(Component.text(PREFIX + "/cloud run <group> (amount)"));
        player.sendMessage(Component.text(PREFIX + "/cloud stop <service>"));
        player.sendMessage(Component.text(PREFIX + "/cloud stopgroup <group>"));
        player.sendMessage(Component.text(PREFIX + "/cloud whitelist (<add/remove>) (<player>)"));
        player.sendMessage(Component.text(PREFIX + "/cloud dispatch <service> <command>"));
        player.sendMessage(Component.text(PREFIX + "/cloud list <players/services>"));
        player.sendMessage(Component.text(PREFIX + "/cloud sync <service>"));
        player.sendMessage(Component.text(PREFIX + "/cloud exit"));
        player.sendMessage(Component.text(PREFIX + "/cloud version"));
        player.sendMessage(Component.text(PREFIX + "/cloud reload"));
    }


    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        String[] args = invocation.arguments();

        if (args.length == 1) {
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
            if (args[0].equalsIgnoreCase("maxplayers")) {
                suggestions.addAll(CloudAPI.getInstance().getGroupsName());
            } else if (args[0].equalsIgnoreCase("stopgroup")) {
                suggestions.addAll(CloudAPI.getInstance().getGroupsName());
            } else if (args[0].equalsIgnoreCase("stop")) {
                suggestions.addAll(CloudAPI.getInstance().getServicePool().getServices()
                        .stream()
                        .map(CloudService::getName)
                        .collect(Collectors.toList()));
            } else if (args[0].equalsIgnoreCase("run") || args[0].equalsIgnoreCase("sync")) {
                suggestions.addAll(CloudAPI.getInstance().getGroupsName());
            } else if (args[0].equalsIgnoreCase("whitelist")) {
                suggestions.add("add");
                suggestions.add("remove");
                suggestions.addAll(CloudAPI.getInstance().getPlayerPool().getPlayers()
                        .stream()
                        .map(CloudPlayer::getUsername)
                        .collect(Collectors.toList()));
            } else if (args[0].equalsIgnoreCase("dispatch")) {
                suggestions.addAll(CloudAPI.getInstance().getServicePool().getServices()
                        .stream()
                        .map(CloudService::getName)
                        .collect(Collectors.toList()));
            } else if (args[0].equalsIgnoreCase("list")) {
                suggestions.add("players");
                suggestions.add("services");
            }
        } else if (args.length == 3) {
            // Additional suggestions for specific cases
            if (args[0].equalsIgnoreCase("run")) {

            } else if (args[0].equalsIgnoreCase("dispatch")) {

            } else if (args[0].equalsIgnoreCase("whitelist")) {
                suggestions.addAll(CloudAPI.getInstance().getPlayerPool().getPlayers()
                        .stream()
                        .map(CloudPlayer::getUsername)
                        .collect(Collectors.toList()));
            }
        }

        // Filter suggestions based on the current input
        String prefix = args[args.length - 1].toLowerCase();
        List<String> filteredSuggestions = suggestions.stream()
                .filter(suggestion -> suggestion.toLowerCase().startsWith(prefix))
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(filteredSuggestions);
    }

}
