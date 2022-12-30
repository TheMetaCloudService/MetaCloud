package eu.themetacloudservice.terminal.screens;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.terminal.enums.Type;

import java.util.Timer;
import java.util.TimerTask;

public class GroupSetup {


    public GroupSetup(String line) {
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 0){
            Driver.getInstance().getTerminalDriver().clearScreen();
            Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
            Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("group", line);
            Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
            Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                    new String[] {"Gewählte GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                            "Welche Art von Server möchten Sie einrichten?",
                            "Mögliche Antworten: §eBUNGEE, LOBBY, GAME"},
                    new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                            "what type of server do you want to setup?",
                            "Possible answers: §eBUNGEE, LOBBY, GAME"});
        } if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 1){
            if (line.equalsIgnoreCase("BUNGEE") || line.equalsIgnoreCase("BUNGEE ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("groupType", "BUNGEE");

                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählte GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählte GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "wie viel Arbeitsspeicher jeder Server maximal haben kann? (in MB)"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "how much memory each server can have at most? (in MB)"});

                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
            }else if (line.equalsIgnoreCase("LOBBY") || line.equalsIgnoreCase("LOBBY ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("groupType", "LOBBY");
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählte GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählte GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "wie viel Arbeitsspeicher jeder Server maximal haben kann? (in MB)"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "how much memory each server can have at most? (in MB)"});

            }else if (line.equalsIgnoreCase("GAME") || line.equalsIgnoreCase("GAME ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("groupType", "GAME");
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählte GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählte GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "wie viel Arbeitsspeicher jeder Server maximal haben kann? (in MB)"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "how much memory each server can have at most? (in MB)"});

            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Bitte geben Sie einen korrekten Gruppentyp an", "please specify a correct group type");
            }
        }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 2){

            if(line.matches("[0-9]+")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("memory", line);
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählte GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählte GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "Gewählte Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory")},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory")});
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "bitte gebe eine zahl an z.b. 512", "please enter a number e.g. 512");
            }
        }
    }

}
