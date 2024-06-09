package eu.metacloudservice.bukkit.command;

import eu.metacloudservice.CloudAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CloudAPI.getInstance().getThisService().shutdown();
        return false;
    }
}
