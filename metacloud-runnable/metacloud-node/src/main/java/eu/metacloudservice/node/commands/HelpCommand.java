package eu.metacloudservice.node.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

@CommandInfo(command = "help", description = "command-help-description", aliases = {"?", "hilfe", "ls"})
public class HelpCommand extends CommandAdapter {


    @Override
    public void performCommand(CommandAdapter command, String[] args) {


        Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().forEach(commandAdapter -> {

            String aliases;
            if (commandAdapter.getAliases().size() == 1){
                aliases = commandAdapter.getAliases().get(0);
            }else {
                aliases = commandAdapter.getAliases().get(0) ;
                for (int i = 1; i != commandAdapter.getAliases().size() ; i++) {
                    aliases = aliases + ", " + commandAdapter.getAliases().get(i);
                }
            }
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " >> §f" + commandAdapter.getCommand() + "  §7'§f"+aliases+"§7' ~ " + Driver.getInstance().getLanguageDriver().getLang().getMessage(commandAdapter.getDescription()));
        });
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
