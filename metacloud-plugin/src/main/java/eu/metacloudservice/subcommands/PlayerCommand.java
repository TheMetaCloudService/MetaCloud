/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.subcommands;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.PluginCommand;
import eu.metacloudservice.api.PluginCommandInfo;
import eu.metacloudservice.api.translate.Translator;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.player.entrys.CloudPlayer;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

@PluginCommandInfo(command = "player", description = "/cloud player")
public class PlayerCommand extends PluginCommand {
    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, org.bukkit.entity.Player bukkitPlayer, String[] args) {
        final String PREFIX = CloudAPI.getInstance().getMessages().getMessages().get("prefix").replace("&", "§");
        if (args.length == 0){
            if (veloPlayer != null){
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player list"));
                veloPlayer.sendMessage(Component.text(PREFIX +  "/cloud player info [player]"));
                veloPlayer.sendMessage(Component.text(PREFIX +"/cloud player whitelist ([add/remove]) ([player])"));
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player dispatch [player] [command]"));
                veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player send [player] [service]"));
            }  else{
                  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX  + "/cloud player list" )));
                  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "/cloud player info [player]")));
                  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "/cloud player whitelist ([add/remove]) ([player])")));
                  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "/cloud player dispatch [player] [command]")));
                  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX + "/cloud player send [player] [service]")));
            }
        }else {
            if (args[0].equalsIgnoreCase("list")){
                if (proxiedPlayer == null) {
                    veloPlayer.sendMessage(Component.text(PREFIX + "List of Players:"));
                    CloudAPI.getInstance().getPlayerPool().getPlayers().forEach(cloudPlayer -> {
                        veloPlayer.sendMessage(Component.text(PREFIX + "§f" + cloudPlayer.getUsername() + " §8| §7service/proxy: §f" + cloudPlayer.getServer().getName() +
                                "/" + cloudPlayer.getProxyServer().getName() ));
                    });
                } else {
                      BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "List of Players:")));
                    CloudAPI.getInstance().getPlayerPool().getPlayers().forEach(cloudPlayer -> {
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "§f" + cloudPlayer.getUsername() + " §8| §7service/proxy: §f" + cloudPlayer.getServer().getName() +
                                "/" + cloudPlayer.getProxyServer().getName())) );
                    });
                }
            }else   if (args[0].equalsIgnoreCase("whitelist")){
                List<String> list = CloudAPI.getInstance().getWhitelist();
                if (args.length == 1){
                    if (list.isEmpty()){
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX + "No players were found on the whitelist"));
                        else
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "No players were found on the whitelist")));
                    }

                    list.forEach(s -> {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX +s));
                        else
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + s)));

                    });
                }else if (args.length == 3){
                    String name = args[2];
                    if (args[1].equalsIgnoreCase("add")){
                        if (!list.contains(name)){
                            if (veloPlayer != null)
                                veloPlayer.sendMessage(Component.text(PREFIX +  "The player " +
                                        "'§f"+name+"§7' is now on the whitelist"));
                            else
                                  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX +  "The player '§f"+name+"§7' is now on the whitelist")));
                            CloudAPI.getInstance().addWhiteList(name);
                        }else {
                            if (veloPlayer != null)
                                veloPlayer.sendMessage(Component.text(PREFIX + "The player '§f"+name+"§7' is already whitelisted"));
                            else
                                  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "The player '§f"+name+"§7' is already whitelisted")));
                        }
                    }else if (args[1].equalsIgnoreCase("remove")){
                        if (list.contains(name)){
                            CloudAPI.getInstance().removeWhiteList(name);
                            if (veloPlayer != null)
                                veloPlayer.sendMessage(Component.text(PREFIX + "The player '§f"+name+"§7' is now no longer on the whitelist"));
                            else
                                  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "The player '§f"+name+"§7' is now no longer on the whitelist")));
                        }else {
                            if (veloPlayer != null)
                                veloPlayer.sendMessage(Component.text(PREFIX + "The player '§f"+name+"§7' is not whitelisted"));
                            else
                                  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "The player '§f"+name+"§7' is not whitelisted")));
                        }
                    }else {
                        if (veloPlayer != null){
                            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player list"));
                            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player info [player]"));
                            veloPlayer.sendMessage(Component.text(PREFIX +"/cloud player whitelist ([add/remove]) ([player])"));
                            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player dispatch [player] [command]"));
                            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player send [player] [service]"));
                        }  else{
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "/cloud player list" )));
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "/cloud player info [player]")));
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "/cloud player whitelist ([add/remove]) ([player])")));
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "/cloud player dispatch [player] [command]")));
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX + "/cloud player send [player] [service]")));
                        }
                    }
                }else {
                    if (veloPlayer != null){
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player list"));
                        veloPlayer.sendMessage(Component.text(PREFIX +  "/cloud player info [player]"));
                        veloPlayer.sendMessage(Component.text(PREFIX +"/cloud player whitelist ([add/remove]) ([player])"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player dispatch [player] [command]"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player send [player] [service]"));
                    }  else{
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "/cloud player list" )));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "/cloud player info [player]")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "/cloud player whitelist ([add/remove]) ([player])")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "/cloud player dispatch [player] [command]")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX + "/cloud player send [player] [service]")));
                    }
                }
            }else  if (args[0].equalsIgnoreCase("dispatch")){
                if (args.length >= 3){
                    StringBuilder msg = new StringBuilder();
                    String player = args[1];
                    for (int i = 2; i < args.length; i++) {
                        msg.append(args[i]).append(" ");
                    }
                    if (CloudAPI.getInstance().getPlayerPool().getPlayers().stream().anyMatch(cloudPlayer -> cloudPlayer.getUsername().equalsIgnoreCase(player))){
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX + "The command '§f"+msg.toString()+"§7' was sent to the player '§f"+player+"§7'"));
                        else
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "The command '§f"+msg.toString()+"§7' was sent to the player '§f"+player+"§7'")));
                        CloudAPI.getInstance().getPlayerPool().getPlayer(player).dispatchCommand(msg.toString());
                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX + "The player you are looking for was not found, please check that it is spelled correctly."));
                        else
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "The player you are looking for was not found, please check that it is spelled correctly.")));
                    }
                }else {
                    if (veloPlayer != null){
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player list"));
                        veloPlayer.sendMessage(Component.text(PREFIX +  "/cloud player info [player]"));
                        veloPlayer.sendMessage(Component.text(PREFIX +"/cloud player whitelist ([add/remove]) ([player])"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player dispatch [player] [command]"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player send [player] [service]"));
                    }  else{
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "/cloud player list" )));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "/cloud player info [player]")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "/cloud player whitelist ([add/remove]) ([player])")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "/cloud player dispatch [player] [command]")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX + "/cloud player send [player] [service]")));
                    }
                }
            }else  if (args[0].equalsIgnoreCase("send")){
                if (args.length == 3){
                    String player = args[1];
                    String service = args[2];
                    if (CloudAPI.getInstance().getPlayerPool().getPlayers().stream().anyMatch(cloudPlayer -> cloudPlayer.getUsername().equalsIgnoreCase(player))){
                        if (CloudAPI.getInstance().getServicePool().getServices().stream().anyMatch(cloudService -> cloudService.getName().equalsIgnoreCase(service))){
                            if (veloPlayer != null)
                                veloPlayer.sendMessage(Component.text(PREFIX + "The player " + player + " has been successfully sent to the " + service + " service."));
                            else
                                  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "The player " + player + " has been successfully sent to the " + service + " service.")));
                            CloudAPI.getInstance().getPlayerPool().getPlayer(player).connect(CloudAPI.getInstance().getServicePool().getService(service));
                        }else {
                            if (veloPlayer != null)
                                veloPlayer.sendMessage(Component.text(PREFIX + "The service you are looking for was not found, please check that it is spelled correctly."));
                            else
                                  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "The service you are looking for was not found, please check that it is spelled correctly.")));
                        }
                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX + "The player you are looking for was not found, please check that it is spelled correctly."));
                        else
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "The player you are looking for was not found, please check that it is spelled correctly.")));
                    }
                }else {
                    if (veloPlayer != null){
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player list"));
                        veloPlayer.sendMessage(Component.text(PREFIX +  "/cloud player info [player]"));
                        veloPlayer.sendMessage(Component.text(PREFIX +"/cloud player whitelist ([add/remove]) ([player])"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player dispatch [player] [command]"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player send [player] [service]"));
                    }  else{
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX + "/cloud player list" )));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "/cloud player info [player]")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "/cloud player whitelist ([add/remove]) ([player])")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "/cloud player dispatch [player] [command]")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX + "/cloud player send [player] [service]")));
                    }
                }
            }else  if (args[0].equalsIgnoreCase("info")){
                if (args.length == 2){
                    String player = args[1];
                    if (CloudAPI.getInstance().getPlayerPool().getPlayers().stream().anyMatch(cloudPlayer -> cloudPlayer.getUsername().equalsIgnoreCase(player))) {
                        CloudPlayer cp = CloudAPI.getInstance().getPlayerPool().getPlayer(player);
                        if (veloPlayer != null){
                            veloPlayer.sendMessage(Component.text(PREFIX + "username: §f" + cp.getUsername()));
                        veloPlayer.sendMessage(Component.text(PREFIX + "uuid: §f" + cp.getUniqueId()));
                        veloPlayer.sendMessage(Component.text(PREFIX + "service: §f" + cp.getServer().getName()));
                        veloPlayer.sendMessage(Component.text(PREFIX + "proxy: §f" + cp.getProxyServer().getName()));
                        veloPlayer.sendMessage(Component.text(PREFIX + "time: §f" + cp.getCurrentPlayTime()));
                        veloPlayer.sendMessage(Component.text(PREFIX + "fallback?: §f" + cp.isConnectedOnFallback()));
                    }  else{
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "username: §f" + cp.getUsername())));
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "uuid: §f" + cp.getUniqueId())));
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "service: §f" + cp.getServer().getName())));
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "proxy: §f" + cp.getProxyServer().getName())));
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX + "time: §f" + cp.getCurrentPlayTime())));
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "fallback?: §f" + cp.isConnectedOnFallback())));
                        }
                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX + "The player you are looking for was not found, please check that it is spelled correctly."));
                        else
                              BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "The player you are looking for was not found, please check that it is spelled correctly.")));
                    }
                }else {
                    if (veloPlayer != null){
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player list"));
                        veloPlayer.sendMessage(Component.text(PREFIX +  "/cloud player info [player]"));
                        veloPlayer.sendMessage(Component.text(PREFIX +"/cloud player whitelist ([add/remove]) ([player])"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player dispatch [player] [command]"));
                        veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player send [player] [service]"));
                    }  else{
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX + "/cloud player list" )));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "/cloud player info [player]")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "/cloud player whitelist ([add/remove]) ([player])")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "/cloud player dispatch [player] [command]")));
                          BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX + "/cloud player send [player] [service]")));
                    }
                }
            }else {
                if (veloPlayer != null){
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player list"));
                    veloPlayer.sendMessage(Component.text(PREFIX +  "/cloud player info [player]"));
                    veloPlayer.sendMessage(Component.text(PREFIX +"/cloud player whitelist ([add/remove]) ([player])"));
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player dispatch [player] [command]"));
                    veloPlayer.sendMessage(Component.text(PREFIX + "/cloud player send [player] [service]"));
                }  else{
                      BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX + "/cloud player list" )));
                      BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(  PREFIX + "/cloud player info [player]")));
                      BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX +  "/cloud player whitelist ([add/remove]) ([player])")));
                      BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate(PREFIX + "/cloud player dispatch [player] [command]")));
                      BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(Component.text(new Translator().translate( PREFIX + "/cloud player send [player] [service]")));
                }
            }
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        List<String> suggestion = new ArrayList<>();
        if (args.length == 0){
            suggestion.add("list");
            suggestion.add("info");
            suggestion.add("whitelist");
            suggestion.add("dispatch");
            suggestion.add("send");
        }else if (args.length == 2 ){
            if (args[0].equalsIgnoreCase("dispatch") || args[0].equalsIgnoreCase("info")|| args[0].equalsIgnoreCase("send")){
                    CloudAPI.getInstance().getPlayerPool().getPlayers().forEach(cloudPlayer -> {
                    suggestion.add(cloudPlayer.getUsername());
                });
            }else if (args[0].equalsIgnoreCase("whitelist")){
                suggestion.add("add");
                suggestion.add("remove");
            }
        } else if (args.length == 3 ){
            if (args[0].equalsIgnoreCase("send")){
                CloudAPI.getInstance().getServicePool().getServices().forEach(cloudService -> suggestion.add(cloudService.getName()));
            }else if (args[0].equalsIgnoreCase("whitelist") && args[1].equalsIgnoreCase("remove")){
                suggestion.addAll(CloudAPI.getInstance().getWhitelist());
            }
        }
        return suggestion;
    }
}
