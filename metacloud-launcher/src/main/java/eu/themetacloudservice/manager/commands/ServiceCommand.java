package eu.themetacloudservice.manager.commands;

import eu.themetacloudservice.terminal.commands.CommandAdapter;
import eu.themetacloudservice.terminal.commands.CommandInfo;
import eu.themetacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

@CommandInfo(command = "tasks", DEdescription = "", ENdescription = "", aliases = {"serv", "task"})
public class ServiceCommand extends CommandAdapter {


    @Override
    public void performCommand(CommandAdapter command, String[] args) {
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
