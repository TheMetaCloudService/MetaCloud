package eu.themetacloudservice.storage;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.commands.CommandAdapter;

public class MessageStorage {
    public String version = "SANDSTORM-1.0.0";
    public String language;

    public MessageStorage() {}

    public String getAsciiArt(){

        if (Driver.getInstance().getTerminalDriver().isInSetup()){
            if (language.equalsIgnoreCase("DE")){
                return
                        "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                                "       §bTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                                "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§b" + version + "§7]\n\n" +
                                "     <§b!§r> zum verlassen des Screens, gebe `§bleave§r` in die Console ein\n" +
                                "     <§b!§r> support: §fmetacloudservice.eu §r(§b@TheMetaCloud§r)\n\n";

            }else return
                    "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                            "       §bTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                            "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§b" + version + "§7]\n\n" +
                            "     <§b!§r> to leave this screen again, type `§bleave§r` in the console \n" +
                            "     <§b!§r> support: §fmetacloudservice.eu §r(§b@TheMetaCloud§r)\n\n";
        }else {
            if (language.equalsIgnoreCase("DE")){
                return
                        "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                                "       §bTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                                "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§b" + version + "§7]\n\n" +
                                "     <§b!§r> Willkommen bei Metacloud, Ihrem intelligentes Cloudsystem für Minecraft§r\n" +
                                "     <§b!§r> support: §fmetacloudservice.eu §r(§b@TheMetaCloud§r)\n\n";

            }else return
                    "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                            "       §bTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                            "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§b" + version + "§7]\n\n" +
                            "     <§b!§r> welcome to Metacloud your intelligent cloud system for minecraft§r\n" +
                            "     <§b!§r> support: §fmetacloudservice.eu §r(§b@TheMetaCloud§r)\n\n";
        }

    }


    public CommandAdapter fromFirstArgument(String commandLine) {
        String[] split = commandLine.split(" ");
        return Driver.getInstance().getTerminalDriver().getCommandDriver().getCommand(split[0]);
    }

    public String[] dropFirstString(String[] input){
        String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }


}
