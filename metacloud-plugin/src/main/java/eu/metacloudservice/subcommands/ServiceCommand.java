/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.subcommands;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.commands.PluginCommand;
import eu.metacloudservice.commands.PluginCommandInfo;
import eu.metacloudservice.commands.translate.Translator;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.configuration.dummys.message.Messages;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

@PluginCommandInfo(command = "service", description = "/cloud service")
public class ServiceCommand extends PluginCommand {
    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, org.bukkit.entity.Player bukkitPlayer, String[] args) {
        Messages messages = CloudAPI.getInstance().getMessages();
        final Translator translator = new Translator();
        String PREFIX = messages.getMessages().get("prefix").replace("&", "§");
        if (args.length == 0){
            if (proxiedPlayer == null){
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
            }else {
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
            }

        }else if (args[0].equalsIgnoreCase("list")){
            CloudAPI.getInstance().getServicePool().getServices().forEach(service -> {
                if (veloPlayer != null)
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "§f" + service.getName() +
                            " §7| GROUP §8⯮ §f" + service.getGroup().getGroup() +
                            "§7 - PLAYERS §8⯮ §f" + service.getPlayers().size() +
                            "§7 ~ STATE §8⯮ §f" + service.getState())));
                else
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "§f" + service.getName() +
                            " §7| GROUP §8⯮ §f" + service.getGroup().getGroup() +
                            "§7 - PLAYERS §8⯮ §f" + service.getPlayers().size() +
                            "§7 ~ STATE §8⯮ §f" + service.getState())));

            });
        }else if (args[0].equalsIgnoreCase("run")){
            if (args.length == 2){
                String group = args[1];
                if (CloudAPI.getInstance().getGroupPool().getGroupsByName().contains(group)){
                    CloudAPI.getInstance().dispatchCommand("service run " + group + " "+  1);
                    if (veloPlayer != null)
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "services have been started from the group you have selected")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "services have been started from the group you have selected")));
                }else {
                    if (veloPlayer != null)
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The group you are looking for was not found, please check that it is spelled correctly.")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.")));
                }
            }else  if (args.length == 3) {
                String group = args[1];
                int amount = Integer.parseInt(args[2]);

                if (CloudAPI.getInstance().getGroupPool().getGroupsByName().contains(group)){
                    CloudAPI.getInstance().dispatchCommand("service run " + group + " "+  amount);
                    if (veloPlayer != null)
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "services have been started from the group you have selected")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "services have been started from the group you have selected")));
                }else {
                    if (veloPlayer != null)
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The group you are looking for was not found, please check that it is spelled correctly.")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.")));
                }
            }else {
                if (proxiedPlayer == null){
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }else {
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }
            }
        }else if (args[0].equalsIgnoreCase("stop")){
            if (args.length >= 2){
                String service = args[1];
                if (CloudAPI.getInstance().getServicePool().serviceNotNull(service)){
                    if (args.length == 2){
                        CloudAPI.getInstance().dispatchCommand("service stop " + service );
                        if (veloPlayer != null)
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The '§f"+service+"§7' service is now stopped")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The '§f"+service+"§7' service is now stopped")));
                    }else {
                        if (proxiedPlayer == null){
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                        }else {
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                        }
                    }
                }else {
                    if (veloPlayer != null)
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The service you are looking for was not found, please check that it is spelled correctly.")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The service you are looking for was not found, please check that it is spelled correctly.")));
                }
            }else {
                if (proxiedPlayer == null){
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }else {
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }
            }
        }else if (args[0].equalsIgnoreCase("stopgroup")){
            if (args.length >= 2){
                String group = args[1];
                if (CloudAPI.getInstance().getGroupPool().getGroupsByName().contains(group)){
                    if (args.length == 2){
                        CloudAPI.getInstance().dispatchCommand("service stopgroup " + group );
                        if (veloPlayer != null)
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The '§f"+group+"§7' group is now stopped")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The '§f"+group+"§7' group is now stopped")));
                    }else {
                        if (proxiedPlayer == null){
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                        }else {
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                        }
                    }
                }else {
                    if (veloPlayer != null)
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The group you are looking for was not found, please check that it is spelled correctly.")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.")));
                }
            }else {
                if (proxiedPlayer == null){
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }else {
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }
            }
        }else if (args[0].equalsIgnoreCase("restart")){
            if (args.length >= 2){
                String service = args[1];
                if (CloudAPI.getInstance().getServicePool().serviceNotNull(service)){
                    if (args.length == 2){
                        CloudAPI.getInstance().dispatchCommand("service restart " + service );
                        if (veloPlayer != null)
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The '§f"+service+"§7' service is now restarting")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The '§f"+service+"§7' service is now restarting")));
                    }else {
                        if (proxiedPlayer == null){
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                        }else {
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                        }

                    }
                }else {
                    if (veloPlayer != null)
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The service you are looking for was not found, please check that it is spelled correctly.")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The service you are looking for was not found, please check that it is spelled correctly.")));
                }
            }else {
                if (proxiedPlayer == null){
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }else {
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }
            }
        }else if (args[0].equalsIgnoreCase("restartgroup")){
                if (args.length >= 2){
                    String group = args[1];
                    if (CloudAPI.getInstance().getGroupPool().getGroupsByName().contains(group)){
                        if (args.length == 2){
                            CloudAPI.getInstance().dispatchCommand("service restartgroup " + group );
                            if (veloPlayer != null)
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The '§f"+group+"§7' group is now restarting")));
                            else
                                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The '§f"+group+"§7' group is now restarting")));
                        }else {
                            if (proxiedPlayer == null){
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                            }else {
                                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                            }
                        }
                    }else {
                        if (veloPlayer != null)
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The group you are looking for was not found, please check that it is spelled correctly.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.")));
                    }
                }else {
                    if (proxiedPlayer == null){
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                    }else {
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                    }
                }
        }else if (args[0].equalsIgnoreCase("copy")){
            if (args.length == 2){
                String service = args[1];
                if (CloudAPI.getInstance().getServicePool().serviceNotNull(service)){

                    CloudAPI.getInstance().getServicePool().getService(service).sync();

                    if (veloPlayer != null)
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The service '§f"+service+"§7' was successfully copied")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The service '§f"+service+"§7' was successfully copied")));
                }else {
                    if (veloPlayer != null)
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The service was not found and therefore cannot be copy")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The service was not found and therefore cannot be copy")));
                }
            }else {
                if (proxiedPlayer == null){
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }else {
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }
            }
        }else if (args[0].equalsIgnoreCase("dispatch")){
            if (args.length >= 3){
                StringBuilder msg = new StringBuilder();
                String service = args[1];
                for (int i = 2; i < args.length; i++) {
                    msg.append(args[i]).append(" ");
                }
                if (CloudAPI.getInstance().getServicePool().getServices().stream().anyMatch(service1 -> service1.getName().equalsIgnoreCase(service))){
                    if (veloPlayer != null)
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The command '§f"+msg.toString()+"§7' was sent to the service '§f"+service+"§7'")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The command '§f"+msg.toString()+"§7' was sent to the service '§f"+service+"§7'")));
                    CloudAPI.getInstance().getServicePool().getService(service).dispatchCommand(msg.toString());
                }else {
                    if (veloPlayer != null)
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The service you are looking for was not found, please check that it is spelled correctly.")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The service you are looking for was not found, please check that it is spelled correctly.")));
                }

            }else {
                if (proxiedPlayer == null){
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }else {
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
                }
            }
        }else {
            if (proxiedPlayer == null){
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
            }else {
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service list")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service run [group] ([amount])")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stop [service]")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service copy [service]")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service stopgroup [group]")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restart [service]")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service restartgroup [group]")));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud service dispatch [service] [command]")));
            }
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        List<String> suggestion = new ArrayList<>();
        if (args.length == 0){
            suggestion.add("list");
            suggestion.add("run");
            suggestion.add("stop");
            suggestion.add("copy");
            suggestion.add("stopgroup");
            suggestion.add("restart");
            suggestion.add("restartgroup");
            suggestion.add("dispatch");
        }else if (args.length == 2 ){
            if (args[0].equalsIgnoreCase("run") || args[0].equalsIgnoreCase("stopgroup") || args[0].equalsIgnoreCase("restartgroup")){
                suggestion.addAll(CloudAPI.getInstance().getGroupPool().getGroupsByName());
            }else if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("restart") || args[0].equalsIgnoreCase("dispatch") || args[0].equalsIgnoreCase("copy")){
                CloudAPI.getInstance().getServicePool().getServices().forEach(cloudService -> suggestion.add(cloudService.getName()));
            }
        }
        return suggestion;
    }

}
