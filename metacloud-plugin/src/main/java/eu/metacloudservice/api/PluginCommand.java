/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.api;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import lombok.var;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class PluginCommand {

    private final String command;

    private final String description;

    public PluginCommand() {
        final var annotation = getClass().getAnnotation(ProxyCommandInfo.class);
        this.command = annotation.command();
        this.description = annotation.description();
    }

    public abstract void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, String[] args);
    public abstract List<String> tabComplete(String[] args);

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
