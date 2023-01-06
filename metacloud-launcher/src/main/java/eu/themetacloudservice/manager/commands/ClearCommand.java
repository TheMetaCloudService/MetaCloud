package eu.themetacloudservice.manager.commands;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.commands.CommandAdapter;
import eu.themetacloudservice.terminal.commands.CommandInfo;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

@CommandInfo(command = "clear", DEdescription = "dieser Befehl löscht die gesamte Konsole", ENdescription = "this command clears the whole console", aliases = {"cls", "cc"})
public class ClearCommand extends CommandAdapter {


    @Override
    public boolean performCommand(CommandAdapter command, String[] args) {
        Driver.getInstance().getTerminalDriver().clearScreen();
        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND, "die Konsole ist jetzt gereinigt worden", "the console has now been cleaned");

        return false;
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
