package eu.themetacloudservice.storage;

public class MessageStorage {
    public String version = "SANDSTORM-1.0.0";
    public String language;

    public MessageStorage() {}

    public String getAsciiArt(){
        if (language.equalsIgnoreCase("DE")){
            return "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                    "       §eTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                    "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§e"+version+"§7]\n" +
                    "       ________________________________________________________\n" +
                    "             §eMODERN§7 MINECRAFT §eCLOUDSYSTEM§7 FOR §eEVERYONE§7                                                        \n" +
                    "\n" +
                    "     <§e!§7> Danke, dass Sie MetaCloud für Ihr Netzwerk nutzen §r\n" +
                    "     <§e!§7> Unseren Support findest du - §ehttps://discord.gg/4kKEcaP9WC\n";
        }else return "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                "       §eTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§e" + version + "§7]\n" +
                "       ________________________________________________________\n" +
                "             §eMODERN§7 MINECRAFT §eCLOUDSYSTEM§7 FOR §eEVERYONE§7                                                        \n" +
                "\n" +
                "     <§e!§7> Thank you for using MetaCloud for your network §r\n" +
                "     <§e!§7> Our Support can you find - §ehttps://discord.gg/4kKEcaP9WC\n";
    }



    public String[] dropFirstString(String[] input){
        String[] string = new String[input.length - 1];
        System.arraycopy(input, 1, string, 0, input.length - 1);
        return string;
    }

}
