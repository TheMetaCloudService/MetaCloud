/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.api;

import com.velocitypowered.api.proxy.Player;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public abstract class PluginCommand {

    private final String command;

    private final String description;

    public PluginCommand() {
        final var annotation = getClass().getAnnotation(PluginCommandInfo.class);
        this.command = annotation.command();
        this.description = annotation.description();
    }

    public abstract void performCommand(final PluginCommand command, final ProxiedPlayer proxiedPlayer, final Player veloPlayer, final org.bukkit.entity.Player bukkitPlayer, final String[] args);
    public abstract List<String> tabComplete(@NonNull  final String[] args);

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
