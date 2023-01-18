package eu.themetacloudservice.manager.commands;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.commands.CommandAdapter;
import eu.themetacloudservice.terminal.commands.CommandInfo;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

@CommandInfo(command = "help", DEdescription = "sehe alle commands auf einen blick ", ENdescription = "see all commands at a glance", aliases = {"?", "hilfe", "ls"})
public class HelpCommand extends CommandAdapter {


    @Override
    public void performCommand(CommandAdapter command, String[] args) {


        Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(commandAdapter -> {

            StringBuilder aliases;
            if (commandAdapter.getAliases().size() == 1){
                aliases = new StringBuilder(commandAdapter.getAliases().get(0));
            }else {
                aliases = new StringBuilder(commandAdapter.getAliases().get(0));
                for (int i = 1; i != commandAdapter.getAliases().size() ; i++) {
                    aliases.append(", ").append(commandAdapter.getAliases().get(i));
                }
            }
            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                    " >> §f" + commandAdapter.getCommand() + "  §7'§f"+aliases+"§7' ~ " + commandAdapter.getDEdescription(),
                    " >> §f" + commandAdapter.getCommand() + "  §7'§f"+aliases+"§7' ~ " + commandAdapter.getENdescription());
        });
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
