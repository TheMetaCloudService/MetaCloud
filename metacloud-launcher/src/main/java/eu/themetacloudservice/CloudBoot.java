package eu.themetacloudservice;

import eu.themetacloudservice.terminal.TerminalDriver;
import eu.themetacloudservice.terminal.enums.Type;
import java.io.File;

public class CloudBoot {


    public static void main(String[] args) {
        new Driver();
        if (!new File("./service.json").exists()){
            Driver.getInstance().getMessageStorage().language = "EN";
        }
        Driver.getInstance().setTerminalDriver(new TerminalDriver());
        Driver.getInstance().getTerminalDriver().clearScreen();
        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
        if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
            Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "es wird geprüft, ob die Cloud bereits eingerichtet ist");
            if (!new File("./service.json").exists()){
                Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "Oh, Sie sind wohl neu hier, dann fangen wir mit der Einrichtung an...");
                try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
                Driver.getInstance().getTerminalDriver().joinSetup();
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "alles perfekt, wir können versuchen, die Cloud zu starten");
            }
        }else {
            Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "it will be checked if the cloud is already set up");
                if (!new File("./service.json").exists()){
                    Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "oh it seems you are new here, we will start the setup then...");
                    try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
                    Driver.getInstance().getTerminalDriver().joinSetup();
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, "everything perfect, we can try to start the cloud");
                }

        }
    }
}
