package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.animation.AnimationDriver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

@CommandInfo(command = "update", DEdescription = "Aktualisierung des gesamten Cloudsystems", ENdescription = "Update the hole cloudsystem", aliases = {"upgrade"})
public class UpdateCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wird geschaut ob die Cloud version noch aktuell ist", "It is checked if the cloud version is still up to date");
        if (Driver.getInstance().getMessageStorage().checkAvailableUpdate()){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Eine neue Version der Metacloud wurde gefunden '§f"+Driver.getInstance().getMessageStorage().version+" >> "+Driver.getInstance().getMessageStorage().getNewVersionName()+"§r'","A new version of the Metacloud was found '§f"+Driver.getInstance().getMessageStorage().version+" >> "+Driver.getInstance().getMessageStorage().getNewVersionName()+"§r'");

            try {
                URLConnection urlConnection = new URL("https://metacloudservice.eu/download/UPDATE.jar").openConnection();
                urlConnection.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                urlConnection.connect();
                Files.copy(urlConnection.getInputStream(), Paths.get("./UPDATE.jar"));
            } catch (IOException ignored) {
            }

            new AnimationDriver().play();
            new File("./local/GLOBAL/EVERY/plugins/metacloud-api.jar").delete();
            new File("./local/GLOBAL/EVERY/plugins/metacloud-plugin.jar").delete();


            Driver.getInstance().getTerminalDriver().log(Type.INFO, "Update §fmetacloud-plugin.jar...");

            try {
                URLConnection urlConnection = new URL("https://metacloudservice.eu/download/metacloud-plugin.jar").openConnection();
                urlConnection.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                urlConnection.connect();
                Files.copy(urlConnection.getInputStream(), Paths.get("./local/GLOBAL/EVERY/plugins/metacloud-plugin.jar"));
            } catch (IOException ignored) {
            }
            new AnimationDriver().play();
            Driver.getInstance().getTerminalDriver().log(Type.INFO, "Update §fmetacloud-api.jar...");

            try {
                URLConnection urlConnection = new URL("https://metacloudservice.eu/download/metacloud-api.jar").openConnection();
                urlConnection.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                urlConnection.connect();
                Files.copy(urlConnection.getInputStream(), Paths.get("./local/GLOBAL/EVERY/plugins/metacloud-api.jar"));
            } catch (IOException ignored) {
            }
            new AnimationDriver().play();


            new File("./Launcher.jar").renameTo(new File("./OLD.jar"));
            new File("./UPDATE.jar").renameTo(new File("./Launcher.jar"));

            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Das Update ist nun bereit, bitte starten Sie die Cloud erneut","The update is now ready, please start the cloud again");

            System.exit(0);


        }else {
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wurde kein Update gefunden, d.h. die Cloud ist auf dem neuesten Stand","No update was found, that means you are up to date");
        }
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
