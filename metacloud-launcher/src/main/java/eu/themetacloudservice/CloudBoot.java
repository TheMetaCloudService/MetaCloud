package eu.themetacloudservice;

import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.node.CloudNode;
import eu.themetacloudservice.terminal.TerminalDriver;
import eu.themetacloudservice.terminal.enums.Type;
import java.io.File;
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
        Driver.getInstance().setTerminalDriver(new TerminalDriver());
        Driver.getInstance().getTerminalDriver().clearScreen();
        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
        if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
            Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "es wird geprüft, ob die Cloud bereits eingerichtet ist");
            if (!new File("./service.json").exists() && !new File("./nodeservice.json").exists()){
                Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "Oh, Sie sind wohl neu hier, dann fangen wir mit der Einrichtung an...");
                try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
                waitForFinishSetup();
                Driver.getInstance().getTerminalDriver().joinSetup();
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "alles perfekt, wir können versuchen, die Cloud zu starten");
                runClient();
            }
        }else {
            Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "it will be checked if the cloud is already set up");
                if (!new File("./service.json").exists() && !new File("./nodeservice.json").exists()){
                    Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "oh it seems you are new here, we will start the setup then...");
                    try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
                    waitForFinishSetup();
                    Driver.getInstance().getTerminalDriver().joinSetup();
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "everything perfect, we can try to start the cloud");
                    runClient();
                }

        }
    }

    public static void runClient(){
        if ( Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
            Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "es wird geschaut ob die Cloud version noch aktuell ist");
            if (Driver.getInstance().getMessageStorage().checkAvailableUpdate()){
                Driver.getInstance().getTerminalDriver().log(Type.WARNING, "eine neue Version der Metacloud wurde gefunden [§e"+Driver.getInstance().getMessageStorage().version+" >> "+Driver.getInstance().getMessageStorage().getNewVersionName()+"§r]");
                Driver.getInstance().getTerminalDriver().log(Type.WARNING, "du können es unter '§ehttps://metacloudservice.eu/downloads/Metacloud-latest.zip§r' herunterladen.");
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "es wurde kein Update gefunden, d.h. die ist auf dem neuesten Stand");
            }
        }else {
            Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "it is checked if the cloud version is still up to date");
            if (Driver.getInstance().getMessageStorage().checkAvailableUpdate()){
                Driver.getInstance().getTerminalDriver().log(Type.WARNING, "a new version of the Metacloud was found [§e"+Driver.getInstance().getMessageStorage().version+" >> "+Driver.getInstance().getMessageStorage().getNewVersionName()+"§r]");
                Driver.getInstance().getTerminalDriver().log(Type.WARNING, "you can download it at '§ehttps://metacloudservice.eu/downloads/Metacloud-latest.zip§r'");
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "no update was found, that means you are up to date");
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
