package eu.themetacloudservice.manager.commands;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.terminal.commands.CommandAdapter;
import eu.themetacloudservice.terminal.commands.CommandInfo;
import eu.themetacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;


@CommandInfo(command = "stop", DEdescription = "Mit diesem Befehl fahren Sie die Cloud herunter", ENdescription = "with this command you shut down the cloud", aliases = {"shutdown", "end"})
public class StopCommand extends CommandAdapter {
    @Override
    public boolean performCommand(CommandAdapter command, String[] args) {
        NettyDriver.getInstance().nettyServer.close();
        System.exit(0);
        System.exit(0);
        System.exit(0);
        return false;
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
