package eu.metacloudservice;

import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.metacloudservice.events.EventDriver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.node.CloudNode;
import eu.metacloudservice.terminal.TerminalDriver;
import eu.metacloudservice.terminal.animation.AnimationDriver;
import eu.metacloudservice.terminal.enums.Type;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class CloudBoot {


    public static void main(String[] args) {
        new Driver();
        if (!new File("./service.json").exists()  && !new File("./nodeservice.json").exists()){
            Driver.getInstance().getMessageStorage().language = "EN";
        }else {
            if (new File("./nodeservice.json").exists()){
                NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
                Driver.getInstance().getMessageStorage().language = config.getLanguage();
            }   if (new File("./service.json").exists()){
                ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                Driver.getInstance().getMessageStorage().language = config.getLanguage();
            }
        }
        Driver.getInstance().getMessageStorage().eventDriver = new EventDriver();
        Driver.getInstance().setTerminalDriver(new TerminalDriver());
        Driver.getInstance().getTerminalDriver().clearScreen();
        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
        String version = System.getProperty("java.version");
        int majorVersion = Integer.parseInt(version.split("\\.")[0]);
        if (majorVersion < 17) {
            System.out.println("Pleas user Java 17 or higher");
            System.exit(0);
            return;
        }
        if (!hasSufficientMemory( Runtime.getRuntime().maxMemory())){
            System.out.println("You need a server with a minimum of 3GB Memory to start the cloud");
            System.exit(0);
            return;
        }
        if (isRootUser(System.getProperty("user.name"))){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "es ist nicht vorteilhaft, die cloud mit dem Root user laufen zulassen", "it is not advantageous to run the cloud with the root user");
        }
        if (new File("./OLD.jar").exists()){
            new File("./OLD.jar").delete();
        }
        if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
            Driver.getInstance().getTerminalDriver().log(Type.INFO, "Es wird geprüft, ob die Cloud bereits eingerichtet ist");
            if (!new File("./service.json").exists() && !new File("./nodeservice.json").exists()){
                Driver.getInstance().getMessageStorage().setupType = "MAINSETUP";
                Driver.getInstance().getTerminalDriver().log(Type.INFO, "Oh, Sie sind wohl neu hier, dann fangen wir mit der Einrichtung an...");
                try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
                waitForFinishSetup();
                Driver.getInstance().getTerminalDriver().joinSetup();
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.INFO, "Alles perfekt, wir können versuchen, die Cloud zu starten");
                runClient();
            }
        }else {
            Driver.getInstance().getTerminalDriver().log(Type.INFO, "It will be checked if the cloud is already set up");
                if (!new File("./service.json").exists() && !new File("./nodeservice.json").exists()){
                    Driver.getInstance().getMessageStorage().setupType = "MAINSETUP";
                    Driver.getInstance().getTerminalDriver().log(Type.INFO, "Oh it seems you are new here, we will start the setup then...");
                    try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
                    waitForFinishSetup();
                    Driver.getInstance().getTerminalDriver().joinSetup();
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.INFO, "Everything perfect, we can try to start the cloud");
                    runClient();
                }
        }
    }


    private static boolean hasSufficientMemory(long availableMemory) {
        // Vergleichen des verfügbaren Speichers mit 3 GB (in Bytes)
        long requiredMemory = 3L * 1024 * 1024 * 1024; // 3 GB in Bytes
        return availableMemory >= requiredMemory;
    }

    private static boolean isRootUser(String username) {
        // Vergleichen des Benutzernamens mit "root" oder anderen bekannten Namen für den Root-Benutzer
        return username.equalsIgnoreCase("root");

}

    public static void runClient(){
        boolean autoUpdate;
        if (new File("./service.json").exists()){
            autoUpdate = ((ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class)).isAutoUpdate();
        }else {
            autoUpdate = ((NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class)).isAutoUpdate();
        }
        if (autoUpdate){
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
        if (new File("./service.json").exists()){
            new CloudManager();
        }else {
            new CloudNode();
        }
    }

    public static void  waitForFinishSetup(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               if (!Driver.getInstance().getTerminalDriver().isInSetup()){
                   runClient();
                   timer.cancel();
               }
            }
        }, 1000, 1000);
    }
}
