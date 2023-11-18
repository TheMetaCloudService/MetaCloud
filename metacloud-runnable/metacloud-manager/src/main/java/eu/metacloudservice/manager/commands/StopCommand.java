package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


@CommandInfo(command = "stop", description = "command-stop-description", aliases = {"shutdown", "end", "quit", "kill"})
public class StopCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (Driver.getInstance().getMessageStorage().shutdownAccept){
            CloudManager.shutdownHook();
        }else {
            Driver.getInstance().getMessageStorage().shutdownAccept = true;
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-stop") );
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Driver.getInstance().getMessageStorage().shutdownAccept = false;
                }
            }, 15*1000);
        }
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
