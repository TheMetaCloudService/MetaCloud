package eu.metacloudservice.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.api.PluginDriver;
import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.pool.player.entrys.CloudPlayer;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CloudCommand implements SimpleCommand {


 


    @SneakyThrows
    @Override
    public void execute(Invocation invocation) {
       String[] args = invocation.arguments();
        if (invocation.source() instanceof  Player player){
            Messages messages = CloudAPI.getInstance().getMessages();
            String PREFIX = messages.getPrefix().replace("&", "§");
            if (player.hasPermission("metacloud.command.use")){
                if (args.length == 0){
                    sendHelp(player);
                }else {
                    if (PluginDriver.getInstance().getCommand(args[0]) != null){
                        String[] refreshedArguments =  Arrays.copyOfRange(args, 1, args.length);
                        PluginDriver.getInstance().getCommand(args[0]).performCommand(PluginDriver.getInstance().getCommand(args[0]),
                                null, player, null, refreshedArguments);
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
        PluginDriver.getInstance().getCommands().forEach(proxyCommand -> {
            player.sendMessage(Component.text(PREFIX + proxyCommand.getDescription()));
        });
    }


    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            PluginDriver.getInstance().getCommands().forEach(proxyCommand -> {
                suggestions.add(proxyCommand.getCommand());
            });
        } else {
            if (PluginDriver.getInstance().getCommand(args[0]) != null) {
                if (args.length == 2){
                    suggestions.addAll(PluginDriver.getInstance().getCommand(args[0]).tabComplete(new String[] {}));
                }else {
                    String[] refreshedArguments =  Arrays.copyOfRange(args, 1, args.length);
                    suggestions.addAll(PluginDriver.getInstance().getCommand(args[0]).tabComplete(refreshedArguments));
                }
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
