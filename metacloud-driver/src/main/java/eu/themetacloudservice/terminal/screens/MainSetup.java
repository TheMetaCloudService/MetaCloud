package eu.themetacloudservice.terminal.screens;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfigNodes;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.terminal.enums.Type;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainSetup {

    public MainSetup(String line){
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 0){
            if (line.equalsIgnoreCase("DE") || line.equalsIgnoreCase("DE ")){
                Driver.getInstance().getMessageStorage().language = "DE";
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("language", "DE");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Bitte geben Sie an, was Sie einrichten möchten?", "Mögliche Antworten: §fMANAGER, §fNODE");

            }else if (line.equalsIgnoreCase("EN") || line.equalsIgnoreCase("EN ")){
                Driver.getInstance().getMessageStorage().language = "EN";
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("language", "EN");
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "please specify what you would like to setup?", "Possible answers: §fMANAGER, §fNODE");
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
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("address", line.replace(" ", ""));
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
                            "Welche Bungeecord version möchtest du verwenden?", "mögliche antworten: §fBUNGEECORD, WATERFALL");

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "bitte gebe eine Ram anzahl an z.b. 512");
                }
            }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 4){

                if (line.equalsIgnoreCase("WATERFALL") | line.equalsIgnoreCase("WATERFALL ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", "WATERFALL");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Welche spigot version möchtest du benutzen?",
                            "mögliche antworten: §fSPIGOT-1.16.5, SPIGOT-1.17.1, SPIGOT-1.18.2, SPIGOT-1.19.2, SPIGOT-1.19.3, PAPER-1.16.5, PAPER-1.17.1, PAPER-1.18.2, PAPER-1.19.2, PAPER-1.19.3");


                }else if (line.equalsIgnoreCase("BUNGEECORD") || line.equalsIgnoreCase("BUNGEECORD ")){

                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", "BUNGEECORD");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Welche spigot version möchtest du benutzen?",
                            "mögliche antworten: §fSPIGOT-1.16.5, SPIGOT-1.17.1, SPIGOT-1.18.2, SPIGOT-1.19.2, SPIGOT-1.19.3, PAPER-1.16.5, PAPER-1.17.1, PAPER-1.18.2, PAPER-1.19.2, PAPER-1.19.3");
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "Die version wurde nicht gefunden");
                }

            }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 5){

                if (line.equalsIgnoreCase("PAPER-1.16.5") || line.equalsIgnoreCase("PAPER-1.16.5 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.16.5");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else  if (line.equalsIgnoreCase("PAPER-1.17.1") || line.equalsIgnoreCase("PAPER-1.17.1 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.17.1");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else if (line.equalsIgnoreCase("PAPER-1.18.2") || line.equalsIgnoreCase("PAPER-1.18.2 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.18.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else if (line.equalsIgnoreCase("PAPER-1.19.2") || line.equalsIgnoreCase("PAPER-1.19.2 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.19.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else if ( line.equalsIgnoreCase("PAPER-1.19.3") || line.equalsIgnoreCase("PAPER-1.19.3 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.19.3");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else  if (line.equalsIgnoreCase("SPIGOT-1.16.5") || line.equalsIgnoreCase("SPIGOT-1.16.5 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.16.5");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else  if (line.equalsIgnoreCase("SPIGOT-1.17.1") || line.equalsIgnoreCase("SPIGOT-1.17.1 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.17.1");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else if (line.equalsIgnoreCase("SPIGOT-1.18.2") || line.equalsIgnoreCase("SPIGOT-1.18.2 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.18.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else if (line.equalsIgnoreCase("SPIGOT-1.19.2") || line.equalsIgnoreCase("SPIGOT-1.19.2 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.19.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else if ( line.equalsIgnoreCase("SPIGOT-1.19.3") || line.equalsIgnoreCase("SPIGOT-1.19.3 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.19.3");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");

                } else {
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
                            "Which bungeecord version do you want to use?", "possible answers: §fBUNGEECORD, WATERFALL");

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
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "which spigot version do you want to use?",
                            "possible answers: §fSPIGOT-1.16.5, SPIGOT-1.17.1, SPIGOT-1.18.2, SPIGOT-1.19.2, SPIGOT-1.19.3, PAPER-1.16.5, PAPER-1.17.1, PAPER-1.18.2, PAPER-1.19.2, PAPER-1.19.3");

                }else if (line.equalsIgnoreCase("BUNGEECORD") || line.equalsIgnoreCase("BUNGEECORD ")){

                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", "BUNGEECORD");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "which spigot version do you want to use?",
                            "possible answers:  §fSPIGOT-1.16.5, SPIGOT-1.17.1, SPIGOT-1.18.2, SPIGOT-1.19.2, SPIGOT-1.19.3, PAPER-1.16.5, PAPER-1.17.1, PAPER-1.18.2, PAPER-1.19.2, PAPER-1.19.3");

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "The version was not found");
                }

            }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 5){


                if (line.equalsIgnoreCase("PAPER-1.16.5") || line.equalsIgnoreCase("PAPER-1.16.5 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.16.5");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");

                } else  if (line.equalsIgnoreCase("PAPER-1.17.1") || line.equalsIgnoreCase("PAPER-1.17.1 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.17.1");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");
                } else if (line.equalsIgnoreCase("PAPER-1.18.2") || line.equalsIgnoreCase("PAPER-1.18.2 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.18.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");
                } else if (line.equalsIgnoreCase("PAPER-1.19.2") || line.equalsIgnoreCase("PAPER-1.19.2 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.19.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");
                } else if ( line.equalsIgnoreCase("PAPER-1.19.3") || line.equalsIgnoreCase("PAPER-1.19.3 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.19.3");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");
                } else  if (line.equalsIgnoreCase("SPIGOT-1.16.5") || line.equalsIgnoreCase("SPIGOT-1.16.5 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.16.5");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");
                } else  if (line.equalsIgnoreCase("SPIGOT-1.17.1") || line.equalsIgnoreCase("SPIGOT-1.17.1 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.17.1");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");
                } else if (line.equalsIgnoreCase("SPIGOT-1.18.2") || line.equalsIgnoreCase("SPIGOT-1.18.2 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.18.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");
                } else if (line.equalsIgnoreCase("SPIGOT-1.19.2") || line.equalsIgnoreCase("SPIGOT-1.19.2 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.19.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");
                } else if ( line.equalsIgnoreCase("SPIGOT-1.19.3") || line.equalsIgnoreCase("SPIGOT-1.19.3 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.19.3");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");

                } else{
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
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Wie soll der Node heißen?");

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "bitte gebe eine Ram anzahl an z.b. 512");
                }
            } if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 4){
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("nodename", line);
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                        "Was ist die Address von diesen Node?");

            } if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 5){
                if (line.contains(".")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("nodeaddress", line);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Welche Bungeecord version möchtest du verwenden?", "mögliche antworten: §bBUNGEECORD, WATERFALL");
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "Ihre IP-Adresse muss Punkte beinhalten");
                }
            } if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 6){
                if (line.equalsIgnoreCase("WATERFALL") | line.equalsIgnoreCase("WATERFALL ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", "WATERFALL");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Welche spigot version möchtest du benutzen?",
                            "mögliche antworten: §bSPIGOT-1.16.5, SPIGOT-1.17.1, SPIGOT-1.18.2, SPIGOT-1.19.2, SPIGOT-1.19.3, PAPER-1.16.5, PAPER-1.17.1, PAPER-1.18.2, PAPER-1.19.2, PAPER-1.19.3");


                }else if (line.equalsIgnoreCase("BUNGEECORD") || line.equalsIgnoreCase("BUNGEECORD ")){

                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", "BUNGEECORD");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Welche spigot version möchtest du benutzen?",
                            "mögliche antworten: §bSPIGOT-1.16.5, SPIGOT-1.17.1, SPIGOT-1.18.2, SPIGOT-1.19.2, SPIGOT-1.19.3, PAPER-1.16.5, PAPER-1.17.1, PAPER-1.18.2, PAPER-1.19.2, PAPER-1.19.3");
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "Die version wurde nicht gefunden");
                }
            } if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 7){
                if (line.equalsIgnoreCase("PAPER-1.16.5") || line.equalsIgnoreCase("PAPER-1.16.5 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.16.5");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else  if (line.equalsIgnoreCase("PAPER-1.17.1") || line.equalsIgnoreCase("PAPER-1.17.1 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.17.1");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else if (line.equalsIgnoreCase("PAPER-1.18.2") || line.equalsIgnoreCase("PAPER-1.18.2 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.18.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else if (line.equalsIgnoreCase("PAPER-1.19.2") || line.equalsIgnoreCase("PAPER-1.19.2 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.19.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");

                } else if ( line.equalsIgnoreCase("PAPER-1.19.3") || line.equalsIgnoreCase("PAPER-1.19.3 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.19.3");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");

                } else  if (line.equalsIgnoreCase("SPIGOT-1.16.5") || line.equalsIgnoreCase("SPIGOT-1.16.5 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.16.5");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");

                } else  if (line.equalsIgnoreCase("SPIGOT-1.17.1") || line.equalsIgnoreCase("SPIGOT-1.17.1 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.17.1");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");

                } else if (line.equalsIgnoreCase("SPIGOT-1.18.2") || line.equalsIgnoreCase("SPIGOT-1.18.2 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.18.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");

                } else if (line.equalsIgnoreCase("SPIGOT-1.19.2") || line.equalsIgnoreCase("SPIGOT-1.19.2 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.19.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();

                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");
                } else if ( line.equalsIgnoreCase("SPIGOT-1.19.3") || line.equalsIgnoreCase("SPIGOT-1.19.3 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.19.3");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §bDE", "Gewählte setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "Gewählte Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "Gewählter Arbeitsspeicher: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "Gewählte Node Name: §b"  + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "Gewählte Node Address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Gewählte Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "Gewählte Spigot Version: §b"+ Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "das Setup ist abgeschlosen, der screen wird gleich geschlossen und die cloud startet");

                } else {
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
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "What should the node be called?");

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "please enter a Ram number e.g. 512");
                }
            }    if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 4){
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("nodename", line);
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                        "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                        "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                        "What is the address of this node?");

            }  if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 5){
                if (line.contains(".")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("nodeaddress", line);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "Which bungeecord version do you want to use?", "possible answers: §bBUNGEECORD, WATERFALL");
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "your IP address must contain dots");
                }
            } if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 6){

                if (line.equalsIgnoreCase("Waterfall") | line.equalsIgnoreCase("Waterfall ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", "WATERFALL");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "which spigot version do you want to use?",
                            "possible answers: §bSPIGOT-1.16.5, SPIGOT-1.17.1, SPIGOT-1.18.2, SPIGOT-1.19.2, SPIGOT-1.19.3, PAPER-1.16.5, PAPER-1.17.1, PAPER-1.18.2, PAPER-1.19.2, PAPER-1.19.3");

                }else  if (line.equalsIgnoreCase("BUNGEECORD") || line.equalsIgnoreCase("BUNGEECORD ")){

                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("version", "BUNGEECORD");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());

                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "which spigot version do you want to use?",
                            "possible answers: §bSPIGOT-1.16.5, SPIGOT-1.17.1, SPIGOT-1.18.2, SPIGOT-1.19.2, SPIGOT-1.19.3, PAPER-1.16.5, PAPER-1.17.1, PAPER-1.18.2, PAPER-1.19.2, PAPER-1.19.3");

                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "The version was not found");
                }
            }if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 7){
                if (line.equalsIgnoreCase("PAPER-1.16.5") || line.equalsIgnoreCase("PAPER-1.16.5 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.16.5");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");
                } else  if (line.equalsIgnoreCase("PAPER-1.17.1") || line.equalsIgnoreCase("PAPER-1.17.1 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.17.1");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");

                } else if (line.equalsIgnoreCase("PAPER-1.18.2") || line.equalsIgnoreCase("PAPER-1.18.2 ") ){
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.18.2");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");

                } else if (line.equalsIgnoreCase("PAPER-1.19.2") || line.equalsIgnoreCase("PAPER-1.19.2 ")){
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.19.2");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");

                } else if ( line.equalsIgnoreCase("PAPER-1.19.3") || line.equalsIgnoreCase("PAPER-1.19.3 ") ){
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "PAPER-1.19.3");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");

                } else  if (line.equalsIgnoreCase("SPIGOT-1.16.5") || line.equalsIgnoreCase("SPIGOT-1.16.5 ")){
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.16.5");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");


                } else  if (line.equalsIgnoreCase("SPIGOT-1.17.1") || line.equalsIgnoreCase("SPIGOT-1.17.1 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.17.1");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");

                } else if (line.equalsIgnoreCase("SPIGOT-1.18.2") || line.equalsIgnoreCase("SPIGOT-1.18.2 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.18.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");
                    createConfiguration();
                } else if (line.equalsIgnoreCase("SPIGOT-1.19.2") || line.equalsIgnoreCase("SPIGOT-1.19.2 ")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.19.2");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");

                } else if ( line.equalsIgnoreCase("SPIGOT-1.19.3") || line.equalsIgnoreCase("SPIGOT-1.19.3 ") ){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("spigotversion", "SPIGOT-1.19.3");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            createConfiguration();
                        }
                    }, 2000);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §bEN", "selected setup: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString(),
                            "selected address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address"), "selected memory: §b"+  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory") + "MB",
                            "selected node name: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename"),
                            "selected node address: §b" + Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress"),
                            "selected Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version"),
                            "selected spigot Version: §b" +  Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion"),
                            "the setup is finished, the screen will be closed and the cloud will be started");

                } else{
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "The version was not found");
                }
            }
        }
    }


    private void createConfiguration(){
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("type").toString().equalsIgnoreCase("MANAGER")){

            Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "The version was not found");
            if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("language").toString().equalsIgnoreCase("DE")){
                ManagerConfig managerConfig = new ManagerConfig();
                ManagerConfigNodes managerConfigNodes = new ManagerConfigNodes();
                managerConfigNodes.setName("InternalNode");
                managerConfigNodes.setAddress("127.0.0.1");

                managerConfig.setLanguage("DE");
                ArrayList<ManagerConfigNodes> nodes = new ArrayList<>();
                nodes.add(managerConfigNodes);
                managerConfig.setManagerAddress(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address").toString());
                managerConfig.setCanUsedMemory(Integer.parseInt(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory").toString()));
                managerConfig.setBungeecordVersion(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version").toString());
                managerConfig.setSpigotVersion( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion").toString());
                managerConfig.setNetworkingCommunication(7002);
                managerConfig.setRestApiCommunication(8097);
                managerConfig.setSplitter("-");
                managerConfig.setNodes(nodes);
                new ConfigDriver("./service.json").save(managerConfig);

                Driver.getInstance().getMessageStorage().language = "DE";
                Driver.getInstance().getTerminalDriver().leaveSetup();

            }else {
                ManagerConfig managerConfig = new ManagerConfig();
                ManagerConfigNodes managerConfigNodes = new ManagerConfigNodes();
                managerConfigNodes.setName("InternalNode");
                managerConfigNodes.setAddress("127.0.0.1");
                managerConfig.setLanguage("EN");
                ArrayList<ManagerConfigNodes> nodes = new ArrayList<>();
                nodes.add(managerConfigNodes);
                managerConfig.setManagerAddress(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address").toString());
                managerConfig.setCanUsedMemory(Integer.parseInt(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory").toString()));
                managerConfig.setBungeecordVersion(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version").toString());
                managerConfig.setSpigotVersion( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion").toString());
                managerConfig.setNetworkingCommunication(7002);
                managerConfig.setSplitter("-");
                managerConfig.setRestApiCommunication(8097);
                managerConfig.setNodes(nodes);
                new ConfigDriver("./service.json").save(managerConfig);
                Driver.getInstance().getMessageStorage().language = "EN";
                Driver.getInstance().getTerminalDriver().leaveSetup();
            }
        }else {
            if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("language").toString().equalsIgnoreCase("DE")){
                NodeConfig config = new NodeConfig();
                config.setLanguage("DE");
                config.setManagerAddress(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address").toString());
                config.setCanUsedMemory(Integer.parseInt(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory").toString()));
                config.setBungeecordVersion(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version").toString());
                config.setSpigotVersion( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion").toString());
                config.setNetworkingCommunication(7002);
                config.setRestApiCommunication(8097);
                config.setNodeAddress(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress").toString());
                config.setNodeName( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename").toString());
                new ConfigDriver("./nodeservice.json").save(config);

                Driver.getInstance().getMessageStorage().language = "DE";
                Driver.getInstance().getTerminalDriver().leaveSetup();
            }else {
                NodeConfig config = new NodeConfig();
                config.setLanguage("EN");
                config.setManagerAddress(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("address").toString());
                config.setCanUsedMemory(Integer.parseInt(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("memory").toString()));
                config.setBungeecordVersion(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("version").toString());
                config.setSpigotVersion( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("spigotversion").toString());
                config.setNetworkingCommunication(7002);
                config.setRestApiCommunication(8097);
                config.setNodeAddress(Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodeaddress").toString());
                config.setNodeName( Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("nodename").toString());
                new ConfigDriver("./nodeservice.json").save(config);
                Driver.getInstance().getMessageStorage().language = "EN";
                Driver.getInstance().getTerminalDriver().leaveSetup();
            }
        }
    }
}
