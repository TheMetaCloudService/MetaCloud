package eu.themetacloudservice.terminal.screens;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfigNodes;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.terminal.enums.Type;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class GroupSetup {


    public GroupSetup(String line) {
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 0){
            Driver.getInstance().getTerminalDriver().clearScreen();
            Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
            Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("group", line);
            Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
            Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                    new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
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
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "wie viel Arbeitsspeicher jeder Server maximal haben kann? (§ein MB§r)"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "how much memory each server can have at most? (§ein MB§r)"});

                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
            }else if (line.equalsIgnoreCase("LOBBY") || line.equalsIgnoreCase("LOBBY ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("groupType", "LOBBY");
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "wie viel Arbeitsspeicher jeder Server maximal haben kann? (§ein MB§r)"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "how much memory each server can have at most? (§ein MB§r)"});

            }else if (line.equalsIgnoreCase("GAME") || line.equalsIgnoreCase("GAME ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("groupType", "GAME");
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "wie viel Arbeitsspeicher jeder Server maximal haben kann? (§ein MB§r)"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "how much memory each server can have at most? (§ein MB§r)"});

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
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "Gewählter Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "Soll die Gruppe statisch laufen? §ey / n"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "should the group run on static? §ey / n"});
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "bitte gebe eine zahl an z.b. 512", "please enter a number e.g. 512");
            }
        }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 3){
            if (line.equalsIgnoreCase("Y") || line.equalsIgnoreCase("Y ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("static", true);
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "Gewählter Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "Gewählt run static: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "Wie viele Spieler sind gleichzeitig auf einem Server erlaubt?"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "selected run static: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "how many players are allowed on one server at the same time?"});

            }else if (line.equalsIgnoreCase("N") || line.equalsIgnoreCase("N ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("static", false);
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "Gewählter Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "Gewählt run static: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "Wie viele Spieler sind gleichzeitig auf einem Server erlaubt?"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "selected run static: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "how many players are allowed on one server at the same time?"});
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Ihre Antwortmöglichkeiten sind nur Y für Ja und N für Nein",
                        "your answer options are only Y for yes and N for no");

            }

        }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 4){
            if(line.matches("[0-9]+")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("players", line);
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "Gewählter Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "Gewählt run static: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "Gewählter player count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "Wie viele Server sollten immer online sein?"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "selected run static: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "selected player count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "how many servers should always be online?"});
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "bitte gebe eine zahl ein",
                        "please enter a number");
            }
        }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 5){
            if(line.matches("[0-9]+")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("ninonline", line);
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "Gewählter Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "Gewählt run static: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "Gewählter player count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "Gewählter server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "Wie viele Server sollten maximal online sein? (§eunlimited = -1§r)"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "selected run static: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "selected player count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "selected server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "What is the maximum number of servers that should be online? (§eunlimited = -1§r)"});
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "bitte gebe eine zahl ein",
                        "please enter a number");
            }
        }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 6){
            if(line.matches("[0-9]+") || line.equalsIgnoreCase("-1")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("maxoneline", line);
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "Gewählter Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "Gewählt run static: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "Gewählter player count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "Gewählter server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "Gewählter max server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                                "ab wie viel Prozent der Spieler sollte ein neuer Server starten ?"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "selected run static: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "selected player count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "selected server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "selected max server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                                "from what percentage of players should a new server start?"});
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "bitte gebe eine zahl ein",
                        "please enter a number");
            }
        }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 7){
            if(line.matches("[0-9]+") && Integer.valueOf(line) <= 100){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("startnew", Integer.valueOf(line));
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "Gewählter Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "Gewählt run static: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "Gewählter player count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "Gewählter server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "Gewählter max server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                                "Gewählt start new %: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("startnew"),
                                "wie viele Server online sein sollten, wenn 100 Spieler in dieser Gruppe sind?"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "selected run static: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "selected player count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "selected server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "selected max server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                                "selected start new %: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("startnew"),
                                "how many servers should be online when 100 players are in this group?"});
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "bitte gebe eine zahl zwischen 1 und 100 ein",
                        "please enter a number between 1 and 100");
            }

            }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 8){
            if(line.matches("[0-9]+")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("group100", Integer.valueOf(line));
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "Gewählter Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "Gewählt run static: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "Gewählter player count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "Gewählter server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "Gewählter max server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                                "Gewählt start new %: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("startnew"),
                                "Gewählt start new Group: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group100"),
                                "wie viele Server online sein sollen, wenn 100 Spieler im Netzwerk sind?"},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "selected run static: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "selected player count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "selected server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "selected max server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                                "selected start new %: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("startnew"),
                                "selected start new Group: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group100"),
                                "how many servers should be online when there are 100 players on the network?"});
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "bitte gebe eine zahl ein",
                        "please enter a number");
            }
        }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 9){
            if(line.matches("[0-9]+")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("network100", Integer.valueOf(line));


                //template liste
                ArrayList<String> templates = Driver.getInstance().getTemplateDriver().get();

                String templateList = null;
                if (templates.isEmpty()){
                    templateList = "CREATE";
                }else {
                    for (int i = 0; i != templates.size() ; i++) {
                        String temp = templates.get(i);
                        templateList = templateList + temp+ ", ";
                    }
                    templateList = templateList + "CREATE";
                }

                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "Gewählter Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "Gewählt run static: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "Gewählter player count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "Gewählter server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "Gewählter max server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                                "Gewählt start new %: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("startnew"),
                                "Gewählt start new Group: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group100"),
                                "Gewählt start new Network: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("network100"),
                                "welches Template soll für die Gruppe genutzt werden? §e" + templateList},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "selected run static: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "selected player count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "selected server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "selected max server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                                "selected start new %: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("startnew"),
                                "selected start new Group: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group100"),
                                "selected start new Network: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("network100"),
                                "which template should be used for the group? §e" + templateList});
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "bitte gebe eine zahl ein",
                        "please enter a number");
            }
        }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 10){
            ArrayList<String> rawtemplates = Driver.getInstance().getTemplateDriver().get();
            ArrayList<String> templates = new ArrayList<>();
            rawtemplates.forEach(s -> {
                templates.add(s);
                templates.add(s+ " ");
            });
            if (templates.contains(line) || line.equalsIgnoreCase("CREATE") || line.equals("CREATE ")){
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("template", line.replace(" ", "").replace("CREATE", Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group").toString()));
                ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                ArrayList<ManagerConfigNodes> configNodes = config.getNodes();
                String nodes = null;
                if (configNodes.size()== 1){
                    nodes = configNodes.get(0).getName();
                }else {
                    for (int i = 0; i != configNodes.size() ; i++) {
                        if (i == configNodes.size()-1){
                            nodes = nodes +configNodes.get(i).getName();
                        }else {
                            nodes = nodes +configNodes.get(i).getName() + ", ";
                        }

                    }
                }


                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                        new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "Gewählter Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "Gewählt run static: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "Gewählter player count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "Gewählter server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "Gewählter max server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                                "Gewählt start new %: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("startnew"),
                                "Gewählt start new Group: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group100"),
                                "Gewählt start new Network: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("network100"),
                                "Gewähltes Template: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("template"),
                                "welchen Node soll diese Gruppe gestartet werden? §e" + nodes},
                        new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                                "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                                "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                                "selected run static: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                                "selected player count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                                "selected server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                                "selected max server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                                "selected start new %: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("startnew"),
                                "selected start new Group: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group100"),
                                "selected start new Network: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("network100"),
                                "selected template: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("template"),
                                "which node should this group be started? §e" + nodes});


            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "bitte gebe ein richtiges Template ein",
                        "please enter a correct template");
            }
        }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 11){
            Driver.getInstance().getTerminalDriver().clearScreen();
            Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("node", line.replace(" ", ""));

            Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
            Driver.getInstance().getTerminalDriver().log(Type.SETUP,
                    new String[] {"Gewählter GroupName: §b"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                            "Gewählter GroupType: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                            "Gewählter Memory: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                            "Gewählt run static: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                            "Gewählter player count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                            "Gewählter server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                            "Gewählter max server count: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                            "Gewählt start new %: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("startnew"),
                            "Gewählt start new Group: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group100"),
                            "Gewählt start new Network: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("network100"),
                            "Gewähltes Template: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("template"),
                            "Gewählter Node: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("node"),
                            "Die Einrichtung der Gruppe ist nun abgeschlossen, der Screen wird nun geschlossen."
                            },
                    new String[] {"selected GroupName: §b" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group"),
                            "selected GroupType. §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("groupType"),
                            "selected Memory: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory"),
                            "selected run static: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("static"),
                            "selected player count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("players"),
                            "selected server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("ninonline"),
                            "selected max server count: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("maxoneline"),
                            "selected start new %: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("startnew"),
                            "selected start new Group: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("group100"),
                            "selected start new Network: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("network100"),
                            "selected template: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("template"),
                            "selected node: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("node"),
                            "The setup of the group is now finished, the screen is about to close"
                            });
        }
    }

}
