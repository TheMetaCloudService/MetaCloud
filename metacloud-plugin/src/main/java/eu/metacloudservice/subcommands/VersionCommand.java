/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.subcommands;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.commands.PluginCommand;
import eu.metacloudservice.commands.PluginCommandInfo;
import eu.metacloudservice.commands.translate.Translator;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.configuration.dummys.message.Messages;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

@PluginCommandInfo(command = "version", description = "/cloud version")
public class VersionCommand extends PluginCommand {
    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, org.bukkit.entity.Player bukkitPlayer, String[] args) {
        final Messages messages = CloudAPI.getInstance().getMessages();
        final String PREFIX = messages.getMessages().get("prefix").replace("&", "§");
        if (proxiedPlayer != null) {
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(new Translator().translate(PREFIX + "The cloud is currently running on version §8⯮ §f" + Driver.getInstance().getMessageStorage().version)));
        } else {
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(new Translator().translate(PREFIX + "The cloud is currently running on version §8⯮ §f" + Driver.getInstance().getMessageStorage().version)));
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return new ArrayList<>();
    }
}
