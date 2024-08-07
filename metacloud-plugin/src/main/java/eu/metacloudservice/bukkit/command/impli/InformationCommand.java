/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bukkit.command.impli;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.PluginCommand;
import eu.metacloudservice.api.PluginCommandInfo;
import eu.metacloudservice.service.entrys.CloudService;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;


@PluginCommandInfo(command =  "information", description = "/service information")
public class InformationCommand extends PluginCommand {

    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, org.bukkit.entity.Player player, String[] args) {
        if (player != null && veloPlayer == null && proxiedPlayer == null){
            final String prefix = CloudAPI.getInstance().getMessages().getMessages().get("prefix");
            final CloudService cloudService = CloudAPI.getInstance().getServicePool().getService(CloudAPI.getInstance().getCurrentService().getService());
            final String maintenance = cloudService.getGroup().isMaintenance() ? "§amaintenance" : "§cmaintenance";
            player.sendMessage(prefix + "Name: §f" + cloudService.getName());
            player.sendMessage(prefix + "Group: §f" + cloudService.getGroup().getGroup() + " §r("+ maintenance +"§r)");
            player.sendMessage(prefix + "State: §f" + cloudService.getState());
            player.sendMessage(prefix + "Host: §f" +cloudService.getAddress() + "§r@§f" + cloudService.getPort());
            player.sendMessage(prefix + "Players: §f" + cloudService.getPlayercount());
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return new ArrayList<>();
    }
}
