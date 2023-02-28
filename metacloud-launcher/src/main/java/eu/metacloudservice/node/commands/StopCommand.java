package eu.metacloudservice.node.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.node.CloudNode;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


@CommandInfo(command = "stop", DEdescription = "Mit diesem Befehl fahren Sie die Cloud herunter", ENdescription = "with this command you shut down the cloud", aliases = {"shutdown", "end"})
public class StopCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {

        if (Driver.getInstance().getMessageStorage().shutdownAccept){
            CloudNode.shutdownHook();
        }else {
            Driver.getInstance().getMessageStorage().shutdownAccept = true;
            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND, "Bitte geben Sie den Befehl für '§fBestätigung§r' erneut ein, Sie haben dafür §f15 Sekunden§r", "please enter the command again for '§fconfirmation§r', you have §f15 seconds §rto do so");
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
