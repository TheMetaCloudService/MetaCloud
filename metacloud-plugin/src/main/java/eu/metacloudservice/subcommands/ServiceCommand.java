/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bungee.command.impli;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.PluginCommand;
import eu.metacloudservice.api.PluginCommandInfo;
import eu.metacloudservice.configuration.dummys.message.Messages;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

@PluginCommandInfo(command = "service", description = "/cloud service")
public class ServiceCommand extends PluginCommand {
    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, org.bukkit.entity.Player bukkitPlayer, String[] args) {
        boolean isBungee = veloPlayer == null;

    }

    public String sendHelp(){
        Messages messages = CloudAPI.getInstance().getMessages();
        String PREFIX = messages.getPrefix().replace("&", "§");
        return PREFIX + "/cloud service ";
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}
