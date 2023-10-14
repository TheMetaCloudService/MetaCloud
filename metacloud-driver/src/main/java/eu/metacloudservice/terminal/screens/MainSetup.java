package eu.metacloudservice.terminal.screens;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfigNodes;
import eu.metacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.groups.dummy.GroupStorage;
import eu.metacloudservice.storage.PacketLoader;
import eu.metacloudservice.terminal.enums.Type;
import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.stream.Collectors;

public class MainSetup {

    public MainSetup(String line){
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 0){
            if (line.equalsIgnoreCase("DE") || line.equalsIgnoreCase("DE ")){
                Driver.getInstance().getMessageStorage().language = "DE";
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("language", "DE");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §fDE", "Bitte geben Sie an, was Sie einrichten möchten?", "Mögliche antworten: §fMANAGER, §fNODE");

            }else if (line.equalsIgnoreCase("EN") || line.equalsIgnoreCase("EN ")){
                Driver.getInstance().getMessageStorage().language = "EN";
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("language", "EN");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Selected language: §fEN", "please specify what you would like to setup?", "Possible answers: §fMANAGER, §fNODE");
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "pleas chose §fDE §7or §fEN");
            }
        }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 1){


            if (line.equalsIgnoreCase("MANAGER") || line.equalsIgnoreCase("MANAGER ")){
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("type", "MANAGER");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, new String[] {"Gewählte Sprache: §fDE", "Gewähltes setup: §fMANAGER", "Wie lautet die Adresse des Managers?"},new String[] {"Selected language: §fEN", "Selected setup: §fMANAGER", "What is the manager's address?"});
            }else if (line.equalsIgnoreCase("NODE") || line.equalsIgnoreCase("NODE ")){
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("type", "NODE");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, new String[] {"Gewählte Sprache: §fDE", "Gewähltes setup: §fNODE", "Wie lautet die Adresse des Managers?"},new String[] {"Selected language: §fEN", "Selected setup: §fNODE", "What is the manager's address?"});

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §fDE", "Gewähltes setup: §fNODE", "Wie lautet die Adresse des Managers?");
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Bitte wählen Sie §fMANAGER §7 oder §fNODE", "Pleas chose §fMANAGER §7or §fNODE");
            }
        }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 2){
            if (line.contains(".")){
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("address", line.replace(" ", ""));
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Wie viel Arbeitsspeicher darf diese Instanz in Anspruch nehmen? (in MB)"};

                String[] en = new String[]{"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "How much memory is this instance allowed to draw? (in MB)"};
                Driver.getInstance().getTerminalDriver().log(Type.SETUP,  de, en);

            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Ihre IP-Adresse muss Punkte beinhalten", "Your IP address must contain dots");

            }
        }else {
            if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("MANAGER")){
                handelSetUpManager(line);
            }else {
                handelSetUpNode(line);
            }
        }
    }


    private void handelSetUpManager(String  line){

        if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 3){
            if(line.matches("[0-9]+")){

                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("memory", line);
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());


                List<String> bungees = new ArrayList<>(new PacketLoader().availableBungeecords());
                StringBuilder available = new StringBuilder();
                if (bungees.size() == 1){
                    available = new StringBuilder(bungees.get(0));
                }else {

                    for (int i = 0; i != bungees.size(); i++){
                        if (i == 0){
                            available = new StringBuilder(bungees.get(0));
                        }else {
                            available.append(", ").append(bungees.get(i));
                        }
                    }
                }


                String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Welche Bungeecord version möchtest du verwenden?", "Mögliche antworten: §f" + available};

                String[] en = new String[]{"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB" ,
                        "Which bungeecord version do you want to use?", "Possible answers: §f" + available};

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);

            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "bitte gebe eine Ram anzahl an z.b. 512",  "please enter a Ram number e.g. 512");
            }
        }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 4){
            List<String> bungees = new PacketLoader().availableBungeecords();
            if (bungees.stream().anyMatch(s -> s.equalsIgnoreCase(line) || s.replace(s, s+ " ").equalsIgnoreCase(line))){

                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", line.replace(" ", "").toUpperCase());
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("chosespigot", "none");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                List<String> spigots = new PacketLoader().availableSpigots();
                List<String> mainSpigots = new ArrayList<>();
                spigots.forEach(s -> {
                    if (!mainSpigots.contains(s.split("-")[0])){
                        mainSpigots.add(s.split("-")[0]);
                    }
                });

                StringBuilder available = new StringBuilder();
                if (mainSpigots.size() == 1){
                    available = new StringBuilder(mainSpigots.get(0));
                }else {

                    for (int i = 0; i != mainSpigots.size(); i++){
                        if (i == 0){
                            available = new StringBuilder(mainSpigots.get(0));
                        }else {
                            available.append(", ").append(mainSpigots.get(i));
                        }
                    }
                }

                String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Gewählte Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Welche spigot version möchtest du benutzen?",
                        "Mögliche antworten: §f" + available};

                String[] en = new String[] {"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"),
                        "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Selected Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Which spigot version do you want to use?",
                        "Possible answers: §f" + available};

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);

            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Die Version wurde nicht gefunden","The version was not found");
            }
        }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 5){

            List<String> mainSpigots = new ArrayList<>();
            new PacketLoader().availableSpigots().forEach(s -> {
                if (!mainSpigots.contains(s.split("-")[0])){
                    mainSpigots.add(s.split("-")[0]);
                }
            });

            if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString().equalsIgnoreCase("none")) {

                if (mainSpigots.stream().anyMatch(s -> s.equalsIgnoreCase(line) || s.replace(s, s + " ").equalsIgnoreCase(line))) {

                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("chosespigot", line.replace(" ", "").toUpperCase());
                    Driver.getInstance().getTerminalDriver().clearScreen();

                    List<String> spigots = new PacketLoader().availableSpigots();
                    List<String> found = new ArrayList<>();
                    spigots.forEach(s -> {
                       if (s.startsWith(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString())){
                           found.add(s);
                       }
                    });

                    StringBuilder available2 = new StringBuilder();
                    if (found.size() == 1){
                        available2 = new StringBuilder(found.get(0));
                    }else {

                        for (int i = 0; i != found.size(); i++){
                            if (i == 0){
                                available2 = new StringBuilder(found.get(0));
                            }else {
                                available2.append(", ").append(found.get(i));
                            }
                        }
                    }
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Sie haben '§f"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString()+"§r' ausgewählt, aber welchen wollen Sie genau?",
                            "Mögliche antworten: §f"+available2+" , BACKTOMAIN"};

                    String[] en = new String[] {"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"),
                            "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Selected Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "you have Selected '§f"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString()+"§r', but which one do you want exactly? ",
                            "Possible answers:  §f"+available2+", BACKTOMAIN"};

                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);


                } else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Die Version wurde nicht gefunden","The version was not found");
                }
            }else if ( new PacketLoader().availableSpigots().stream()
                    .filter(s -> s.startsWith(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString()))
                    .collect(Collectors.toList()).stream().anyMatch(s -> s.equalsIgnoreCase(line) || s.replace(s, s+ " ").equalsIgnoreCase(line))){

                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", line.replace(" ", "").toUpperCase());
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;

                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Gewählte Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Gewählte Spigot Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                        "Sollen standartgemäß eine Lobby & Proxy Gruppe erstellt werden? §fY / N"};

                String[] en = new String[]{"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Selected Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Selected spigot Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                        "Should a lobby & proxy group be created as standard? §fY / N"};

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);

            }else if (line.equalsIgnoreCase("BACKTOMAIN") || line.equalsIgnoreCase("BACKTOMAIN ")) {
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("chosespigot", "none");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                StringBuilder available = new StringBuilder();
                if (mainSpigots.size() == 1){
                    available = new StringBuilder(mainSpigots.get(0));
                }else {

                    for (int i = 0; i != mainSpigots.size(); i++){
                        if (i == 0){
                            available = new StringBuilder(mainSpigots.get(0));
                        }else {
                            available.append(", ").append(mainSpigots.get(i));
                        }
                    }
                }

                String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Gewählte Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Welche spigot version möchtest du benutzen?",
                        "Mögliche antworten: §f" + available};

                String[] en = new String[] {"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"),
                        "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Selected Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Which spigot version do you want to use?",
                        "Possible answers: §f" + available};

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);
            }else {

                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Die Version wurde nicht gefunden","The version was not found");
            }
        }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 6){
            if (line.equalsIgnoreCase("Y") || line.equalsIgnoreCase("N") ||line.equalsIgnoreCase("Y ") || line.equalsIgnoreCase("N ") ){

                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("defaultgroups", line.replace(" ", "").toUpperCase());
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;

                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Gewählte Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Gewählte Spigot Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                        "Erstellen von Standardgruppen: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("defaultgroups"),
                        "Sollen Spieler, die den Server betreten, angezeigt werden? §fY / N"};

                String[] en = new String[]{"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Selected Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Selected spigot Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                        "Creating default groups: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("defaultgroups"),
                        "Should players entering the server be displayed? §fY / N"};

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Ihre Antwortmöglichkeiten sind nur Y für Ja und N für Nein",
                        "your answer options are only Y for yes and N for no");
            }
        }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 7){
            if (line.equalsIgnoreCase("Y") || line.equalsIgnoreCase("N") ||line.equalsIgnoreCase("Y ") || line.equalsIgnoreCase("N ")){

                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("joingplayers", line.replace(" ", "").toUpperCase());
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;

                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Gewählte Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Gewählte Spigot Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                        "Erstellen von Standardgruppen: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("defaultgroups"),
                        "Spieler anzeigen, wenn sie beitreten: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("joingplayers"),
                        "Sollte das protocol verwendet werden? §fY / N "};

                String[] en = new String[]{"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Selected Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Selected spigot Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                        "Creating default groups: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("defaultgroups"),
                        "Showing players when they join: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("joingplayers"),
                        "Should the protocol be used? §fY / N"};

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Ihre Antwortmöglichkeiten sind nur Y für Ja und N für Nein",
                        "your answer options are only Y for yes and N for no");
            }
        }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 8){
            if (line.equalsIgnoreCase("Y") || line.equalsIgnoreCase("N") ||line.equalsIgnoreCase("Y ") || line.equalsIgnoreCase("N ")){

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        createConfiguration();
                    }
                }, 2000);
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("protocol", line.replace(" ", "").toUpperCase());

                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Gewählte Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Gewählte Spigot Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                        "Erstellen von Standardgruppen: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("defaultgroups"),
                        "Spieler anzeigen, wenn sie beitreten: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("joingplayers"),
                        "Gewählte das protocol soll verwendet werden: §f" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("protocol"),
                        "Die Einrichtung ist nun abgeschlossen, die Cloudsteht in den Startlöchern...."};

                String[] en = new String[]{"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Selected Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Selected spigot Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                        "Creating default groups: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("defaultgroups"),
                        "Showing players when they join: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("joingplayers"),
                        "Selected protocol will be used: §f" +Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("protocol"),
                        "The setup is now complete, the cloud is about to start...."};

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Ihre Antwortmöglichkeiten sind nur Y für Ja und N für Nein",
                        "your answer options are only Y for yes and N for no");
            }
        }

    }


    private void handelSetUpNode(String  line){
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 3){
            if(line.matches("[0-9]+")){
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("memory", line);
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                String[] de = new String[] {"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Wie soll der Node heißen?"};
                String[] en = new String[] {"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "What should the node be called?"};

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);
                return;

            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "bitte gebe eine Ram anzahl an z.b. 512", "please enter a Ram number e.g. 512");
            }
        }  if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 4){
            Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
            Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("nodename", line);
            Driver.getInstance().getTerminalDriver().clearScreen();
            Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
            String[] de = new String[] {"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                    "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"),
                    "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                    "Gewählter name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                    "Was ist die Address von diesem Node?"};
            String[] en = new String[] {"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                    "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                    "Selected node name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                    "What is the address of this node?"};
            Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);
        } if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 5){
            if (line.contains(".")){
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("nodeaddress", line);


                List<String> bungees = new PacketLoader().availableBungeecords();
                StringBuilder available = new StringBuilder();
                if (bungees.size() == 1){
                    available = new StringBuilder(bungees.get(0));
                }else {

                    for (int i = 0; i != bungees.size(); i++){
                        if (i == 0){
                            available = new StringBuilder(bungees.get(0));
                        }else {
                            available.append(", ").append(bungees.get(i));
                        }
                    }
                }
                Driver.getInstance().getTerminalDriver().clearScreen();
                String[] de = new String[] {"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"),
                        "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Gewählter name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                        "Welche Bungeecord version möchtest du verwenden?", "Mögliche antworten: §f" + available};
                String[] en = new String[] {"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Selected node name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                        "Selected node address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                        "Which bungeecord version do you want to use?", "Possible answers: §f" + available};
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);
                return;
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "", "your IP address must contain dots");
            }
        } if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 6) {
            List<String> bungees = new PacketLoader().availableBungeecords();
            if (bungees.stream().anyMatch(s -> s.equalsIgnoreCase(line) || s.replace(s, s + " ").equalsIgnoreCase(line))) {

                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", line.replace(" ", "").toUpperCase());
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("chosespigot", "none");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                List<String> spigots = new PacketLoader().availableSpigots();
                List<String> mainSpigots = new ArrayList<>();
                spigots.forEach(s -> {
                    if (!mainSpigots.contains(s.split("-")[0])) {
                        mainSpigots.add(s.split("-")[0]);
                    }
                });

                StringBuilder available = new StringBuilder();
                if (mainSpigots.size() == 1) {
                    available = new StringBuilder(mainSpigots.get(0));
                } else {

                    for (int i = 0; i != mainSpigots.size() ; i++) {
                        if (i == 0) {
                            available = new StringBuilder(mainSpigots.get(0));
                        } else {
                            available.append(", ").append(mainSpigots.get(i));
                        }
                    }
                }
                String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"),
                        "Gewählter Arbeitsspeicher: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Gewählter name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                        "Gewählte node Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                        "Welche Spigot version möchtest du verwenden?",
                        "Mögliche antworten: §f" + available};

                String[] en = new String[]{"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Selected node name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                        "Selected node address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                        "Which spigot version do you want to use?",
                        "Possible answers: §f" + available};
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);
            }
        } if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 7) {
            List<String> mainSpigots = new ArrayList<>();
            new PacketLoader().availableSpigots().forEach(s -> {
                if (!mainSpigots.contains(s.split("-")[0])){
                    mainSpigots.add(s.split("-")[0]);
                }
            });

            if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString().equalsIgnoreCase("none")) {

                if (mainSpigots.stream().anyMatch(s -> s.equalsIgnoreCase(line) || s.replace(s, s + " ").equalsIgnoreCase(line))) {

                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("chosespigot", line.replace(" ", "").toUpperCase());
                    Driver.getInstance().getTerminalDriver().clearScreen();

                    List<String> spigots = new PacketLoader().availableSpigots();
                    List<String> found = new ArrayList<>();
                    spigots.forEach(s -> {
                        if (s.startsWith(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString())){
                            found.add(s);
                        }
                    });

                    StringBuilder available2 = new StringBuilder();
                    if (found.size() == 1){
                        available2 = new StringBuilder(found.get(0));
                    }else {

                        for (int i = 0; i != found.size(); i++){
                            if (i == 0){
                                available2 = new StringBuilder(found.get(0));
                            }else {
                                available2.append(", ").append(found.get(i));
                            }
                        }
                    }
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"),
                            "Gewählter Arbeitsspeicher: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählter name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte node Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählter name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Sie haben '§f"+Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString()+"§r' ausgewählt, aber welchen wollen Sie genau?",
                            "Mögliche antworten: §f"+available2+" , BACKTOMAIN"};

                    String[] en = new String[] {"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Selected node name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Selected node address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Selected Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),toString()+"§r', but which one do you want exactly? ",
                            "Possible answers: §f"+available2+", BACKTOMAIN"};

                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);


                } else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Die Version wurde nicht gefunden","The version was not found");
                }
            }else if ( new PacketLoader().availableSpigots().stream()
                    .filter(s -> s.startsWith(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("chosespigot").toString()))
                    .collect(Collectors.toList()).stream().anyMatch(s -> s.equalsIgnoreCase(line) || s.replace(s, s+ " ").equalsIgnoreCase(line))){

                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", line.replace(" ", "").toUpperCase());

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        createConfiguration();
                    }
                }, 2000);
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Gewählter name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                        "Gewählte node Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                        "Gewählte Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Gewählte Spigot Version: §f"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                        "Das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet"};

                String[] en = new String[]{"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Selected node name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                        "Selected node address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                        "Selected Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                        "Selected spigot Version: §f" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),

                        "The setup is finished, the screen will be closed and the cloud will be started"};

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);

            }else if (line.equalsIgnoreCase("BACKTOMAIN") || line.equalsIgnoreCase("BACKTOMAIN ")) {
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("chosespigot", "none");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                StringBuilder available = new StringBuilder();
                if (mainSpigots.size() == 1){
                    available = new StringBuilder(mainSpigots.get(0));
                }else {

                    for (int i = 0; i != mainSpigots.size(); i++){
                        if (i == 0){
                            available = new StringBuilder(mainSpigots.get(0));
                        }else {
                            available.append(", ").append(mainSpigots.get(i));
                        }
                    }
                }

                String[] de = new String[]{"Gewählte Sprache: §fDE", "Gewählte setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"),
                        "Gewählter Arbeitsspeicher: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Gewählter name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                        "Gewählte node Address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                        "Welche Spigot version möchtest du verwenden?",
                        "Mögliche antworten: §f" + available};

                String[] en = new String[]{"Selected language: §fEN", "Selected setup: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Selected address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Selected memory: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Selected node name: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                        "Selected node address: §f" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                        "Which spigot version do you want to use?",
                        "Possible answers: §f" + available};

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, de, en);
            }else {

                Driver.getInstance().getTerminalDriver().logSpeed(Type.SETUP_ERROR, "Die Version wurde nicht gefunden", "The version was not found");
            }
        }
    }


    private void createConfiguration(){
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("MANAGER")){
            ManagerConfig managerConfig = new ManagerConfig();
            ManagerConfigNodes managerConfigNodes = new ManagerConfigNodes();
            managerConfigNodes.setName("InternalNode");
            managerConfigNodes.setAddress(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address").toString());
            managerConfig.setLanguage(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("language").toString());
            ArrayList<ManagerConfigNodes> nodes = new ArrayList<>();
            nodes.add(managerConfigNodes);
            managerConfig.setManagerAddress(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address").toString());
            managerConfig.setCanUsedMemory(Integer.parseInt(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory").toString()));
            managerConfig.setBungeecordVersion(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version").toString());
            managerConfig.setSpigotVersion( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion").toString());
            managerConfig.setNetworkingCommunication(7002);
            managerConfig.setSplitter("#");
            managerConfig.setUseProtocol(Boolean.parseBoolean(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("protocol").toString()
                    .replace("Y" , "true").replace("N", "false")));
            managerConfig.setCopyLogs(true);
            managerConfig.setProcessorUsage(90);
            managerConfig.setTimeOutCheckTime(120);
            managerConfig.setServiceStartupCount(4);
            managerConfig.setRestApiCommunication(8097);
            managerConfig.setShowConnectingPlayers(Boolean.parseBoolean(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("joingplayers").toString()
                    .replace("Y" , "true").replace("N", "false")));
            managerConfig.setUuid("INT");
            managerConfig.setBungeecordPort(25565);
            managerConfig.setSpigotPort(5000);
            managerConfig.setAutoUpdate(true);
            managerConfig.setWhitelist(new ArrayList<>());
            managerConfig.setNodes(nodes);
            new ConfigDriver("./service.json").save(managerConfig);
            Driver.getInstance().getMessageStorage().language = Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("language").toString();

            if (Boolean.parseBoolean(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("joingplayers").toString()
                    .replace("Y" , "true").replace("N", "false"))){
                Driver.getInstance().getGroupDriver()
                        .create(new Group("Proxy","PROXY", 256, true, false, 0,"", 512, 1, -1, 90, 1, 1, new GroupStorage("Proxy", "InternalNode", "", ""), 0));
                Driver.getInstance().getGroupDriver()
                        .create(new Group("Lobby","LOBBY", 1024, true, false, 0,"", 50, 1, -1, 90, 3, 3, new GroupStorage("Proxy", "InternalNode", "", ""), 1));
            }

            Driver.getInstance().getTerminalDriver().leaveSetup();

        }else {
            NodeConfig config = new NodeConfig();
            config.setLanguage(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("language").toString());
            config.setManagerAddress(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address").toString());
            config.setCanUsedMemory(Integer.parseInt(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory").toString()));
            config.setBungeecordVersion(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version").toString());
            config.setSpigotVersion(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion").toString());
            config.setNetworkingCommunication(7002);
            config.setRestApiCommunication(8097);
            config.setCopyLogs(true);
            config.setBungeecordPort(25565);
            config.setProcessorUsage(90);
            config.setAutoUpdate(true);
            config.setSpigotPort(5000);
            config.setNodeAddress(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress").toString());
            config.setNodeName(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename").toString());
            new ConfigDriver("./nodeservice.json").save(config);
            Driver.getInstance().getMessageStorage().language = Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("language").toString();
            Driver.getInstance().getTerminalDriver().leaveSetup();
        }
    }
}