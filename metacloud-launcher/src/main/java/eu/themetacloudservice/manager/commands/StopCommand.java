package eu.themetacloudservice.manager.commands;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.networking.NettyDriver;
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
    public boolean performCommand(CommandAdapter command, String[] args) {

        if (Driver.getInstance().getMessageStorage().shutdownAccept){
            CloudManager.shutdownHook();
        }else {
            Driver.getInstance().getMessageStorage().shutdownAccept = true;
            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND, "Bitte geben Sie den Befehl für §bBestätigung§r erneut ein, Sie haben dafür §b15 Sekunden§r", "please enter the command again for §bconfirmation§r, you have §b15 seconds §rto do so");

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Driver.getInstance().getMessageStorage().shutdownAccept = false;
                }
            }, 15*1000);
        }
        return false;
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
