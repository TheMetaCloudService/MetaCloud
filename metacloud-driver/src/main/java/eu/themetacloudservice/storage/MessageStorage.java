package eu.themetacloudservice.storage;

import com.google.gson.JsonObject;
import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.updateconfig.UpdateConfig;
import eu.themetacloudservice.terminal.commands.CommandAdapter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

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


    @SneakyThrows
    public boolean checkAvailableUpdate() {

        final InputStream inputStream = new URL("https://metacloudservice.eu/rest/cloudversion.php").openStream();
        final StringBuilder builder = new StringBuilder();

        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            UpdateConfig updateConfig = (UpdateConfig) new ConfigDriver().convert(rawJson, UpdateConfig.class);
            if (!updateConfig.getLatestReleasedVersion().equalsIgnoreCase(version)) {
                return true;
            }
        } finally {
            bufferedReader.close();
            inputStream.close();
        }


        return false;
    }

    @SneakyThrows
    public String  getNewVersionName() {

        final InputStream inputStream = new URL("https://metacloudservice.eu/rest/cloudversion.php").openStream();
        final StringBuilder builder = new StringBuilder();

        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            UpdateConfig updateConfig = (UpdateConfig) new ConfigDriver().convert(rawJson, UpdateConfig.class);
            if (!updateConfig.getLatestReleasedVersion().equalsIgnoreCase(version)) {
                return updateConfig.getLatestReleasedVersion();
            }
        } finally {
            bufferedReader.close();
            inputStream.close();
        }


        return "";
    }

}
