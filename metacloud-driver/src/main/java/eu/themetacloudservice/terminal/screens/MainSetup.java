package eu.themetacloudservice.terminal.screens;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.enums.Type;

public class MainSetup {

    public MainSetup(String line){
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 0){
            if (line.equalsIgnoreCase("DE") || line.equalsIgnoreCase("DE ")){
                Driver.getInstance().getMessageStorage().language = "DE";
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("language", "DE");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Bitte geben Sie an, was Sie einrichten möchten?", "Mögliche Antworten: §bMANAGER, §bNODE");

            }else if (line.equalsIgnoreCase("EN") || line.equalsIgnoreCase("EN ")){
                Driver.getInstance().getMessageStorage().language = "EN";
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("language", "EN");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "please specify what you would like to setup?", "Possible answers: §bMANAGER, §bNODE");
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "pleas chose §bDE §7or §bEN");
            }
        }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 1){
            if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("language").toString().equalsIgnoreCase("DE")){
                if (line.equalsIgnoreCase("MANAGER") || line.equalsIgnoreCase("MANAGER ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("type", "MANAGER");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewähltes setup: §bMANAGER", "Wie lautet die Adresse des Managers?");
                }else if (line.equalsIgnoreCase("NODE") || line.equalsIgnoreCase("NODE ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("type", "NODE");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewähltes setup: §bNODE", "Wie lautet die Adresse des Managers?");

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "Bitte wählen Sie §bMANAGER §7 oder §bNODE");
                }
            }else {
                if (line.equalsIgnoreCase("MANAGER") || line.equalsIgnoreCase("MANAGER ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("type", "MANAGER");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §bMANAGER", "what is the manager's address?");
                }else if (line.equalsIgnoreCase("NODE") || line.equalsIgnoreCase("NODE ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("type", "NODE");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §bNODE", "what is the manager's address?");
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "pleas chose §bMANAGER §7or §bNODE");
                }
            }

        }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 2){
            if (line.contains(".")){
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("address", line);
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("language").toString().equalsIgnoreCase("DE")){

                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Wie viel Arbeitsspeicher darf diese Instanz in Anspruch nehmen? (in MB)");

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "how much memory is this instance allowed to draw? (in MB)");
                }
            }else {
                if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("language").toString().equalsIgnoreCase("DE")){
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "Ihre IP-Adresse muss Punkte beinhalten");
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "your IP address must contain dots");
                }
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
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("language").toString().equalsIgnoreCase("DE")){
            if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 3){
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("memory", line);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Welche Bungeecord version möchtest du verwenden?", "mögliche antworten: §bWaterfall, Velocity");

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "bitte gebe eine Ram anzahl an z.b. 512");
                }
            }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 4){

                if (line.equalsIgnoreCase("Waterfall") | line.equalsIgnoreCase("Waterfall ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", "WATERFALL");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Welche spigot version möchtest du benutzen?",
                            "mögliche antworten: §bpaper-1.16.5, paper-1.17.1, paper-1.18.2, paper-1.19.2, paper-1.19.3");


                }else if (line.equalsIgnoreCase("Velocity") || line.equalsIgnoreCase("Velocity ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", "VELOCITY");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §v"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version")
                            "Welche spigot version möchtest du benutzen?",
                            "mögliche antworten: §bpaper-1.16.5, paper-1.17.1, paper-1.18.2, paper-1.19.2, paper-1.19.3");

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "Die version wurde nicht gefunden");
                }

            }
        }else {
            if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 3){
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("memory", line);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB" ,
                            "Which bungeecord version do you want to use?", "possible answers: §bWaterfall, Velocity");

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "please enter a Ram number e.g. 512");
                }
            }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 4){

                if (line.equalsIgnoreCase("Waterfall") | line.equalsIgnoreCase("Waterfall ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", "WATERFALL");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version")
                            "Welche spigot version möchtest du benutzen?",
                            "mögliche antworten: §bpaper-1.16.5, paper-1.17.1, paper-1.18.2, paper-1.19.2, paper-1.19.3");

                }else if (line.equalsIgnoreCase("Velocity") || line.equalsIgnoreCase("Velocity ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", "VELOCITY");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version")
                            "Welche spigot version möchtest du benutzen?",
                            "mögliche antworten: §bpaper-1.16.5, paper-1.17.1, paper-1.18.2, paper-1.19.2, paper-1.19.3");
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "The version was not found");
                }

            }
        }
    }


    private void handelSetUpNode(String  line){
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("language").toString().equalsIgnoreCase("DE")){
            if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 3){
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("memory", line);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB"  );

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "bitte gebe eine Ram anzahl an z.b. 512");
                }
            }
        }else {
            if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 3){
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("memory", line);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB"   );

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "please enter a Ram number e.g. 512");
                }
            }
        }
    }
}
