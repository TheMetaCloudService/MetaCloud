package eu.metacloudservice.bungee.command;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.webserver.dummys.WhiteList;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.function.Consumer;

public class CloudCommand extends Command {
    public CloudCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {


        if (commandSender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            Messages messages = CloudAPI.getInstance().getMessages();
            String PREFIX = Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§");
            if (player.hasPermission("metacloud.command.use")){
                if (args.length == 0){
                    sendHelp(player);
                }else if (args[0].equalsIgnoreCase("maintenance")){
                    if (args.length == 2){
                        String group = args[1];
                        if (CloudAPI.getInstance().getGroupsName().contains(group)){
                            Group groupc = CloudAPI.getInstance().getGroups().parallelStream().filter(group1 -> group1.getGroup().equalsIgnoreCase(group)).findFirst().get();
                            if (groupc.isMaintenance()){
                                CloudAPI.getInstance().dispatchCommand("group " + group + " setmaintenance false");
                                player.sendMessage(PREFIX + "The group is now no longer in maintenance");
                            }else {

                                CloudAPI.getInstance().dispatchCommand("group " + group + " setmaintenance true");
                                player.sendMessage(PREFIX + "The group is now in maintenance");
                            }
                        }else {
                            player.sendMessage(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.");
                        }
                    }else {
                        sendHelp(player);
                    }
                }else if (args[0].equalsIgnoreCase("setmaxplayers")){
                    if (args.length == 3){
                        String group = args[1];
                        String count = args[2];
                        if (CloudAPI.getInstance().getGroupsName().contains(group)){

                            CloudAPI.getInstance().dispatchCommand("group " + group + " setmaxplayers " + count);
                            player.sendMessage(PREFIX + "The number of players has been adjusted");

                        }else {
                            player.sendMessage(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.");
                        }
                    }else {
                        sendHelp(player);
                    }

                } else if (args[0].equalsIgnoreCase("whitelist")){
                    List<String> list = CloudAPI.getInstance().getWhitelist();
                    if (args.length == 1){
                        list.forEach(s -> {
                            player.sendMessage(PREFIX + s);
                        });
                    }else if (args.length == 2){
                        String name = args[2];
                        if (args[1].equalsIgnoreCase("add")){
                            if (list.contains(name)){
                                player.sendMessage(PREFIX +  "The player is now on the whitelist");
                                CloudAPI.getInstance().addWhiteList(name);
                            }else {
                                player.sendMessage(PREFIX +  "the player is already whitelisted");
                            }
                        }else if (args[1].equalsIgnoreCase("remove")){
                            if (!list.contains(name)){
                                CloudAPI.getInstance().removeWhiteList(name);
                                player.sendMessage(PREFIX +  "The player is now no longer on the whitelist");
                            }else {
                                player.sendMessage(PREFIX +  "the player is not whitelisted");
                            }
                        }else {
                            sendHelp(player);
                        }
                    }else {
                        sendHelp(player);
                    }

                } else if (args[0].equalsIgnoreCase("run")){
                    if (args.length == 3){
                        String group = args[1];
                        String count = args[2];
                        if (CloudAPI.getInstance().getGroupsName().contains(group)){

                            CloudAPI.getInstance().dispatchCommand("service run " + group + " "+  count);
                            player.sendMessage(PREFIX + "services have been started from the group you have selected");

                        }else {
                            player.sendMessage(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.");
                        }
                    }else {
                        sendHelp(player);
                    }
                } else if (args[0].equalsIgnoreCase("stop")){
                    if (args.length >= 2){
                        String group = args[1];
                        if (CloudAPI.getInstance().getServicePool().getService(group) != null){
                            if (args.length == 2){
                                CloudAPI.getInstance().dispatchCommand("service stop " + group );
                            }else if (args.length == 3){
                                if (args[3].equalsIgnoreCase("--force")){
                                    CloudAPI.getInstance().dispatchCommand("service stop " + group + " --force");
                                }else {
                                    sendHelp(player);
                                }
                            }else {
                                sendHelp(player);
                            }
                        }else {
                            player.sendMessage(PREFIX + "The service you are looking for was not found, please check that it is spelled correctly.");
                        }
                    }else {
                        sendHelp(player);
                    }

                } else if (args[0].equalsIgnoreCase("gstop")){
                    if (args.length >= 2){
                        String group = args[1];
                        if (CloudAPI.getInstance().getGroupsName().contains(group)){
                            if (args.length == 2){
                                CloudAPI.getInstance().dispatchCommand("service stopgroup " + group );
                            }else if (args.length == 3){
                                if (args[3].equalsIgnoreCase("--force")){
                                    CloudAPI.getInstance().dispatchCommand("service stopgroup " + group + " --force");
                                }else {
                                    sendHelp(player);
                                }
                            }else {
                                sendHelp(player);
                            }
                        }else {
                            player.sendMessage(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.");
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
                            player.sendMessage(PREFIX + "The command was sent to the service");
                            CloudAPI.getInstance().getServicePool().getService(service).dispatchCommand(msg.toString());
                        }else {
                            player.sendMessage(PREFIX + "The service you are looking for was not found, please check that it is spelled correctly.");
                        }

                    }else {
                        sendHelp(player);
                    }

                } else if (args[0].equalsIgnoreCase("sync")){
                    if (args.length == 2){
                        String service = args[1];
                        if (CloudAPI.getInstance().getServicePool().getServices().stream().anyMatch(service1 -> service1.getName().equalsIgnoreCase(service))){
                            player.sendMessage(PREFIX + "The searched service was successfully synchronized");
                            CloudAPI.getInstance().getServicePool().getService(service).sync();
                        }else {
                            player.sendMessage(PREFIX + "The service you are looking for was not found, please check that it is spelled correctly.");
                        }
                        return;
                    }else {
                        sendHelp(player);
                    }

                } else if (args[0].equalsIgnoreCase("listServices")){
                    CloudAPI.getInstance().getServicePool().getServices().forEach(service -> {
                        player.sendMessage(PREFIX + service.getName() + " -> §f" + service.getPlayers().size() + " players");
                    });
                } else if (args[0].equalsIgnoreCase("listgroups")){
                    CloudAPI.getInstance().getGroupsName().forEach(group -> {
                        player.sendMessage(PREFIX + group);
                    });
                } else if (args[0].equalsIgnoreCase("listPlayers")){
                    CloudAPI.getInstance().getPlayerPool().getPlayers().forEach(cloudPlayer -> {
                        player.sendMessage(PREFIX + cloudPlayer.getUsername() + " -> §f" + cloudPlayer.getServer().getName());
                    });
                } else if (args[0].equalsIgnoreCase("version")){
                    player.sendMessage(PREFIX + "current version of metacloud: §f" + Driver.getInstance().getMessageStorage().version);
                } else if (args[0].equalsIgnoreCase("reload")){
                    if (args.length == 1){
                        player.sendMessage(PREFIX + "it tries to reload the cloud, the packets were sent to the manager");
                        CloudAPI.getInstance().dispatchCommand("reload");
                        return;
                    }else {
                        sendHelp(player);
                    }
                } else{
                    sendHelp(player);
                }


            }else {
                player.sendMessage(PREFIX + "The MetaCloud was developed by §fRauchigesEtwas §r| VERSION: §f" + Driver.getInstance().getMessageStorage().version);
                player.sendMessage(PREFIX + "You can §fdownload§7 them from our website '§fhttps://metacloudservice.eu§7'.");
            }
        }

    }



    public void sendHelp(ProxiedPlayer player){
        Messages messages = CloudAPI.getInstance().getMessages();
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud maintenance <group>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud setmaxplayers <group> <count>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud whitelist (<add : remove>) (<name>)");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud run <group> <count>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud stop <service> --force");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud gstop <group> --force");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud dispatch <service> <command>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud sync <service>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud listServices");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud listgroups");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud listPlayers");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud version");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud reload");
    }

}
