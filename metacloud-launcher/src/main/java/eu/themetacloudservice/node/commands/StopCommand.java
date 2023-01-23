package eu.themetacloudservice.node.commands;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.node.CloudNode;
import eu.themetacloudservice.terminal.commands.CommandAdapter;
import eu.themetacloudservice.terminal.commands.CommandInfo;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.terminal.utils.TerminalStorageLine;

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
