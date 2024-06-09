package eu.metacloudservice.bungee.command;

import eu.metacloudservice.CloudAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class EndCommand extends Command {
    public EndCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        CloudAPI.getInstance().getThisService().shutdown();
    }
}
