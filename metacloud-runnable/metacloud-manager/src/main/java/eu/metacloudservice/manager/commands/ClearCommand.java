package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

@CommandInfo(command = "clear",description = "command-clear-description", aliases = {"cls", "cc", "clearline"})
public class ClearCommand extends CommandAdapter {


    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        Driver.getInstance().getTerminalDriver().clearScreen();
        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-clear-successful"));
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
