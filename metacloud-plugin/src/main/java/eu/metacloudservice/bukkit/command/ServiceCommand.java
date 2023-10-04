/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bukkit.command;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.api.PluginCommand;
import eu.metacloudservice.api.PluginDriver;
import net.kyori.adventure.text.Component;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof  Player player){
            if (player.hasPermission("metacloud.command.service")){
                if (args.length == 0){
                    sendHelp(player);
                }else {
                    if (PluginDriver.getInstance().getCommand(args[0]) != null){
                        PluginCommand pluginCommand = PluginDriver.getInstance().getCommand(args[0]);
                        String[] argsUpdate = Arrays.copyOfRange(args, 1, args.length);
                        pluginCommand.performCommand(pluginCommand, null, null, player, argsUpdate);
                    }else {
                        sendHelp(player);
                    }
                }
            }else {
                player.sendMessage("§8▷ §7The network uses §bMetacloud§8 [§a"+ Driver.getInstance().getMessageStorage().version+"§8]");
                player.sendMessage("§8▷ §fhttps://metacloudservice.eu/");
            }
        }
        return false;
    }


    private void sendHelp(Player player){
        PluginDriver.getInstance().getCommands().forEach(pluginCommand -> {
            player.sendMessage(CloudAPI.getInstance().getMessages().getPrefix() + pluginCommand.getDescription());
        });
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1){
            PluginDriver.getInstance().getCommands().forEach(pluginCommand -> {
                suggestions.add(pluginCommand.getCommand());
            });
        }else {
            if (PluginDriver.getInstance().getCommand(args[0]) != null) {
                if (args.length == 2){
                    suggestions.addAll(PluginDriver.getInstance().getCommand(args[0]).tabComplete(new String[] {}));
                }else {
                    String[] refreshedArguments =  Arrays.copyOfRange(args, 1, args.length);
                    suggestions.addAll(PluginDriver.getInstance().getCommand(args[0]).tabComplete(refreshedArguments));
                }
            }
        }
        return suggestions;
    }
}
