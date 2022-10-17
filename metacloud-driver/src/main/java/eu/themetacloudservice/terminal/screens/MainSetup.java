package eu.themetacloudservice.terminal.screens;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.enums.Type;

public class MainSetup {

    public MainSetup(String line){
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 0){
            if (line.equalsIgnoreCase("DE")){
                Driver.getInstance().getMessageStorage().language = "DE";
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, "               _______ _______ _______ _     _  _____ \n" +
                        "               |______ |______    |    |     | |_____]\n" +
                        "               ______| |______    |    |_____| |      \n" +
                        "       ________________________________________________________\n" +
                        "             §eMODERN§7 MINECRAFT §eCLOUDSYSTEM§7 FOR §eEVERYONE§7                                                        \n" +
                        "\n" +
                        "     <§e!§7> die Einrichtung ist §astarted§7, bitte beantworten Sie alle meine §eFragen §r\n" +
                        "     <§e!§7> Sie können die Einrichtung jederzeit mit \"§eleave§7\" verlassen\n");

                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §eDE", "Welche Datenbak möchten Sie verwenden?", "Typ: §eARANGODB");

            }else if (line.equalsIgnoreCase("EN")){
                Driver.getInstance().getMessageStorage().language = "EN";
                Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                Driver.getInstance().getTerminalDriver().clearScreen();
                Driver.getInstance().getTerminalDriver().log(Type.EMPTY, "               _______ _______ _______ _     _  _____ \n" +
                        "               |______ |______    |    |     | |_____]\n" +
                        "               ______| |______    |    |_____| |      \n" +
                        "       ________________________________________________________\n" +
                        "             §eMODERN§7 MINECRAFT §eCLOUDSYSTEM§7 FOR §eEVERYONE§7                                                        \n" +
                        "\n" +
                        "     <§e!§7> the setup has been §astarted§7, please answer all my §equestions §r\n" +
                        "     <§e!§7> you can leave the setup at any time with \"§eleave§7\"\n");
                Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §eEN", "What datenbak do you want to use?", "types: §eARANGODB");
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "pleas chose §eDE §7or §eEN");
            }
        }else if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 1){
            if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
                if (line.equalsIgnoreCase("arangodb")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("db-type", "arangodb");
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, "               _______ _______ _______ _     _  _____ \n" +
                            "               |______ |______    |    |     | |_____]\n" +
                            "               ______| |______    |    |_____| |      \n" +
                            "       ________________________________________________________\n" +
                            "             §eMODERN§7 MINECRAFT §eCLOUDSYSTEM§7 FOR §eEVERYONE§7                                                        \n" +
                            "\n" +
                            "     <§e!§7> die Einrichtung ist §astarted§7, bitte beantworten Sie alle meine §eFragen §r\n" +
                            "     <§e!§7> Sie können die Einrichtung jederzeit mit \"§eleave§7\" verlassen\n");

                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §eDE", "Gewählte Datenbank: §eARANGODB", "Wie lautet der Host der Datenbank?");
                }else {

                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "Bitte geben Sie einen Datenbanktyp an");
                }
            }else {

                if (line.equalsIgnoreCase("arangodb")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("db-type", "arangodb");
                    Driver.getInstance().getTerminalDriver().clearScreen();

                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, "               _______ _______ _______ _     _  _____ \n" +
                            "               |______ |______    |    |     | |_____]\n" +
                            "               ______| |______    |    |_____| |      \n" +
                            "       ________________________________________________________\n" +
                            "             §eMODERN§7 MINECRAFT §eCLOUDSYSTEM§7 FOR §eEVERYONE§7                                                        \n" +
                            "\n" +
                            "     <§e!§7> the setup has been §astarted§7, please answer all my §equestions §r\n" +
                            "     <§e!§7> you can leave the setup at any time with \"§eleave§7\"\n");
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §eEN", "selected database: §eARANGODB", "What is the database host?");
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "please specify a database type ");
                }
            }
        }else {
            if (Driver.getInstance().getTerminalDriver().getSetupStorage().storage.get("db-type").toString().equalsIgnoreCase("arangodb")) {
                setupArangoDB(line);
            }
        }
    }

    private void setupArangoDB(String line){
        if (Driver.getInstance().getTerminalDriver().getSetupStorage().step == 2){
            if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
                if (!line.matches("[a-z]+") && !line.matches("[A-Z]+") && line.contains(".")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("db-host", line);
                    Driver.getInstance().getTerminalDriver().clearScreen();

                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, "               _______ _______ _______ _     _  _____ \n" +
                            "               |______ |______    |    |     | |_____]\n" +
                            "               ______| |______    |    |_____| |      \n" +
                            "       ________________________________________________________\n" +
                            "             §eMODERN§7 MINECRAFT §eCLOUDSYSTEM§7 FOR §eEVERYONE§7                                                        \n" +
                            "\n" +
                            "     <§e!§7> die Einrichtung ist §astarted§7, bitte beantworten Sie alle meine §eFragen §r\n" +
                            "     <§e!§7> Sie können die Einrichtung jederzeit mit \"§eleave§7\" verlassen\n");

                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "Gewählte Sprache: §eDE", "Gewählte Datenbank: §eARANGODB", "ausgewählter db-host: §e" +line);

                }else{
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "bitte eine IP-Adresse wie 127.0.0.1 angeben");
                }
            }else{
                if (!line.matches("[a-z]+") && !line.matches("[A-Z]+") && line.contains(".")){
                    Driver.getInstance().getTerminalDriver().getSetupStorage().step++;
                    Driver.getInstance().getTerminalDriver().getSetupStorage().storage.put("db-host", line);
                    Driver.getInstance().getTerminalDriver().clearScreen();
                    Driver.getInstance().getTerminalDriver().log(Type.EMPTY, "               _______ _______ _______ _     _  _____ \n" +
                            "               |______ |______    |    |     | |_____]\n" +
                            "               ______| |______    |    |_____| |      \n" +
                            "       ________________________________________________________\n" +
                            "             §eMODERN§7 MINECRAFT §eCLOUDSYSTEM§7 FOR §eEVERYONE§7                                                        \n" +
                            "\n" +
                            "     <§e!§7> the setup has been §astarted§7, please answer all my §equestions §r\n" +
                            "     <§e!§7> you can leave the setup at any time with \"§eleave§7\"\n");
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP, "selected language: §eEN", "selected database: §eARANGODB", "selected db-host: §e" + line);

                }else{
                    Driver.getInstance().getTerminalDriver().log(Type.SETUP_ERROR, "please specify an IP address like 127.0.0.1");
                }
            }
        }
    }
}
