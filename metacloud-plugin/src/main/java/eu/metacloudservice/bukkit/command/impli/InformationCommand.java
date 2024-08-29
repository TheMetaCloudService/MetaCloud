/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bukkit.command.impli;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.commands.PluginCommand;
import eu.metacloudservice.commands.PluginCommandInfo;
import eu.metacloudservice.commands.translate.Translator;
import eu.metacloudservice.bukkit.BukkitBootstrap;
import eu.metacloudservice.service.entrys.CloudService;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;


@PluginCommandInfo(command =  "information", description = "/service information")
public class InformationCommand extends PluginCommand {

    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, org.bukkit.entity.Player player, String[] args) {
        final Translator translator = new Translator();
        if (player != null && veloPlayer == null && proxiedPlayer == null){
            final String prefix = CloudAPI.getInstance().getMessages().getMessages().get("prefix");
            final CloudService cloudService = CloudAPI.getInstance().getServicePool().getService(CloudAPI.getInstance().getCurrentService().getService());
            final String maintenance = cloudService.getGroup().isMaintenance() ? "§amaintenance" : "§cmaintenance";
            BukkitBootstrap.audience.player(player).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(prefix + "Name: §f" + cloudService.getName())));
            BukkitBootstrap.audience.player(player).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(prefix + "Group: §f" + cloudService.getGroup().getGroup() + " §r("+ maintenance +"§r)")));
            BukkitBootstrap.audience.player(player).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(prefix + "State: §f" + cloudService.getState())));
            BukkitBootstrap.audience.player(player).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(prefix + "Host: §f" +cloudService.getAddress() + "§r@§f" + cloudService.getPort())));
            BukkitBootstrap.audience.player(player).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(prefix + "Players: §f" + cloudService.getPlayercount())));
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return new ArrayList<>();
    }
}
