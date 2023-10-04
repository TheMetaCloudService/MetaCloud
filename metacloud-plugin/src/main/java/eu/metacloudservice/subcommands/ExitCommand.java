/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.subcommands;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.PluginCommand;
import eu.metacloudservice.api.PluginCommandInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

@PluginCommandInfo(command = "exit", description = "/cloud exit")
public class ExitCommand extends PluginCommand {
    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, org.bukkit.entity.Player bukkitPlayer, String[] args) {
        CloudAPI.getInstance().dispatchCommand("stop");
        CloudAPI.getInstance().dispatchCommand("stop");
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return new ArrayList<>();
    }
}
