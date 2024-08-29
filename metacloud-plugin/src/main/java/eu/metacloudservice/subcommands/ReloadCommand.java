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

@PluginCommandInfo(command = "reload", description = "/cloud reload")
public class ReloadCommand extends PluginCommand {

    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, org.bukkit.entity.Player bukkitPlayer, String[] args) {
        final Translator translator = new Translator();
        if (args.length == 0){
            if (proxiedPlayer == null){
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(getHelp())));
            }else {
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(getHelp())));
            }
        }else {
            final Messages messages = CloudAPI.getInstance().getMessages();
            final String PREFIX = messages.getMessages().get("prefix").replace("&", "§");
            if (args[0].equalsIgnoreCase("all")){
                if (proxiedPlayer == null){
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "the whole cloud was reloaded")));
                }else {
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "the whole cloud was reloaded")));
                }
                CloudAPI.getInstance().dispatchCommand("reload all");
            }else if (args[0].equalsIgnoreCase("modules")){
                if (proxiedPlayer == null){
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "the modules was reloaded")));
                }else {
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "the modules was reloaded")));
                }
                CloudAPI.getInstance().dispatchCommand("reload modules");
            }else if (args[0].equalsIgnoreCase("config")){
                if (proxiedPlayer == null){
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "the config was reloaded")));
                }else {
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "the config was reloaded")));
                }
                CloudAPI.getInstance().dispatchCommand("reload config");
            }else {
                if (proxiedPlayer == null){
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(getHelp())));
                }else {
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(getHelp())));
                }
            }
        }
    }

    private String getHelp(){
        final  Messages messages = CloudAPI.getInstance().getMessages();
        final  String PREFIX = messages.getMessages().get("prefix").replace("&", "§");
        return PREFIX + "/cloud reload <all/config/modules>";

    }

    @Override
    public List<String> tabComplete(final String[] args) {
        final  List<String > suggestion =  new ArrayList<>();
        if (args.length == 0){
            suggestion.add("all");
            suggestion.add("config");
            suggestion.add("modules");
        }
        return suggestion;
    }
}
