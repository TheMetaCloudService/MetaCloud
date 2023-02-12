package eu.themetacloudservice;

import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.node.CloudNode;
import eu.themetacloudservice.terminal.TerminalDriver;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.terminal.streams.TerminalError;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;

public class CloudBoot {


    public static void main(String[] args) {
        new Driver();



        System.setOut(new PrintStream(new TerminalError(Type.INFO), true));
        System.setErr(new PrintStream(new TerminalError(Type.ERROR), true));
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
        Driver.getInstance().setTerminalDriver(new TerminalDriver());
        Driver.getInstance().getTerminalDriver().clearScreen();
        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
        if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
            Driver.getInstance().getTerminalDriver().log(Type.INFO, "Es wird geprüft, ob die Cloud bereits eingerichtet ist");
            if (!new File("./service.json").exists() && !new File("./nodeservice.json").exists()){
                Driver.getInstance().getMessageStorage().setuptype = "MAINSETUP";
                Driver.getInstance().getTerminalDriver().log(Type.INFO, "Oh, Sie sind wohl neu hier, dann fangen wir mit der Einrichtung an...");
                try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
                waitForFinishSetup();
                Driver.getInstance().getTerminalDriver().joinSetup();
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.INFO, "Alles perfekt, wir können versuchen, die Cloud zu starten");
                runClient();
            }
        }else {
            Driver.getInstance().getTerminalDriver().log(Type.INFO, "it will be checked if the cloud is already set up");
                if (!new File("./service.json").exists() && !new File("./nodeservice.json").exists()){
                    Driver.getInstance().getMessageStorage().setuptype = "MAINSETUP";
                    Driver.getInstance().getTerminalDriver().log(Type.INFO, "oh it seems you are new here, we will start the setup then...");
                    try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
                    waitForFinishSetup();
                    Driver.getInstance().getTerminalDriver().joinSetup();
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.INFO, "everything perfect, we can try to start the cloud");
                    runClient();
                }

        }
    }

    public static void runClient(){
       if ( Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
            Driver.getInstance().getTerminalDriver().log(Type.INFO, "Es wird geschaut ob die Cloud version noch aktuell ist");
            if (Driver.getInstance().getMessageStorage().checkAvailableUpdate()){
                Driver.getInstance().getTerminalDriver().log(Type.WARN, "Eine neue Version der Metacloud wurde gefunden '§f"+Driver.getInstance().getMessageStorage().version+" >> "+Driver.getInstance().getMessageStorage().getNewVersionName()+"§r'");
                Driver.getInstance().getTerminalDriver().log(Type.WARN, "Du können es unter '§fhttps://metacloudservice.eu/downloads/Metacloud-latest.zip§r' herunterladen.");
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.INFO, "Es wurde kein Update gefunden, d.h. die ist auf dem neuesten Stand");
            }
        }else {
            Driver.getInstance().getTerminalDriver().log(Type.INFO, "it is checked if the cloud version is still up to date");
            if (Driver.getInstance().getMessageStorage().checkAvailableUpdate()){
                Driver.getInstance().getTerminalDriver().log(Type.WARN, "a new version of the Metacloud was found '§f"+Driver.getInstance().getMessageStorage().version+" >> "+Driver.getInstance().getMessageStorage().getNewVersionName()+"§r'");
                Driver.getInstance().getTerminalDriver().log(Type.WARN, "you can download it at '§fhttps://metacloudservice.eu/downloads/Metacloud-latest.zip§r'");
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.INFO, "no update was found, that means you are up to date");
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
