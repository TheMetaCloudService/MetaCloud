package eu.metacloudservice.manager.commands;

import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

@CommandInfo(command = "module" , DEdescription = "", ENdescription = "", aliases = {"addons", "applications", "modules", "plugins"})
public class ModuleCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {

    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
