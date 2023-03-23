package eu.metacloudservice.node;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.metacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.metacloudservice.networking.in.node.PacketInAuthNode;
import eu.metacloudservice.networking.in.node.PacketInNodeActionSuccess;
import eu.metacloudservice.networking.in.node.PacketInShutdownNode;
import eu.metacloudservice.networking.out.node.*;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.client.NettyClient;
import eu.metacloudservice.node.cloudservices.CloudServiceDriver;
import eu.metacloudservice.node.commands.HelpCommand;
import eu.metacloudservice.node.commands.StopCommand;
import eu.metacloudservice.node.networking.*;
import eu.metacloudservice.terminal.enums.Type;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CloudNode {

    public  static  CloudServiceDriver cloudServiceDriver;

    public CloudNode() {
        new File("./modules/").mkdirs();
        new File("./local/GLOBAL/plugins/").mkdirs();
        new File("./local/templates/").mkdirs();
        cloudServiceDriver = new CloudServiceDriver();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Es wird versucht, den '§fCloud Node§r' zu starten",
                "an attempt is made to start the '§fcloud node§r'");
        NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
        System.setProperty("log4j.configurationFile", "log4j2.properties");
        if (!new File("./connection.key").exists()){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Die '§fconnection.key§r' config wurde nicht gefunden!", "the '§fconnection.key§r' config was not found!");

            System.exit(0);
        }
        if (!new File("./local/server-icon.png").exists()){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Versuche die Datei '§fserver-icon.png§r' herunter zuladen",
                    "Try to download the file '§fserver-icon.png§r'.");
            Driver.getInstance().getMessageStorage().packetLoader.loadLogo();
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Der download war erfolgreich",
                    "The download was successful");
        }else {
        }

        if (!new File("./local/GLOBAL/EVERY/plugins/metacloud-plugin.jar").exists()){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Versuche die Datei '§fmetacloud-plugin.jar§r' herunter zuladen",
                    "Try to download the file '§fmetacloud-plugin.jar§r'.");
            Driver.getInstance().getMessageStorage().packetLoader.loadPlugin();
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Der download war erfolgreich",
                    "The download was successful");
        }else {
        }
        initNetty(config);

        if (config.isUseViaVersion()){
            if (!new File("./local/GLOBAL/EVERY/plugins/viaversion-latest.jar").exists()){
                Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Versuche die Datei '§fviaversion-latest.jar§r' herunter zuladen",
                        "Try to download the file '§fviaversion-latest.jar§r'.");
                try {
                    URLConnection urlConnection = new URL("https://github.com/ViaVersion/ViaVersion/releases/download/4.5.1/ViaVersion-4.5.1.jar").openConnection();
                    urlConnection.setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    urlConnection.connect();
                    Files.copy(urlConnection.getInputStream(), Paths.get("local/GLOBAL/EVERY/plugins/viaversion-latest.jar"));
                } catch (IOException ignored) {

                }
                Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Der download war erfolgreich",
                        "The download was successful");
            }else {

            }
        }

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wird versucht, alle Befehle zu laden und ihre Bereitstellung deutlich zu machen",
                "it is tried to load all commands and to make the provision of them clear");
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new HelpCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new StopCommand());


        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wurden '§f"+Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().size()+" Befehle§r' gefunden und geladen",
                "there were '§f"+Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().size()+" commands§r'  found and loaded");

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Die Cloud erfolgreich gestartet ist, können Sie sie von nun an mit '§fhelp§r' nutzen.",
                "the cloud is successfully started, you can use it from now on with '§fhelp§r'.");
        AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInAuthNode(config.getNodeName(), Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey())));
    }


    public static void shutdownHook(){

        NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);


        cloudServiceDriver.shutdownALLFORCE();

        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInShutdownNode(config.getNodeName()));
        NettyDriver.getInstance().nettyClient.close();

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Danke für die Nutzung von MetaCloud ;->",
                "thank you for using MetaCloud ;->");
        System.exit(0);
    }

    private void initNetty(NodeConfig config){
        new NettyDriver();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Netty-Client wird vorbereitet und dann gestartet", "the Netty client is prepared and then started");

        NettyDriver.getInstance().nettyClient = new NettyClient();
         NettyDriver.getInstance().nettyClient.bind(config.getManagerAddress(), config.getNetworkingCommunication()).connect();

        NettyDriver.getInstance().packetDriver

                /*
                * in this part all packages and trader sent form the server are registered
                * {@link NettyAdaptor} handles the packet and looks where it belongs
                * {@link Packet} handles the packets are written and read via a ByteBuf
                * */
                .registerHandler(new PacketOutAuthSuccess().getPacketUUID(), new HandlePacketOutAuthSuccess(), PacketOutAuthSuccess.class)
                .registerHandler(new PacketOutStopService().getPacketUUID(), new HandlePacketOutStopService(), PacketOutStopService.class)
                .registerHandler(new PacketOutLaunchService().getPacketUUID(), new HandlePacketOutLaunchService(), PacketOutLaunchService.class)
                .registerHandler(new PacketOutShutdownNode().getPacketUUID(), new HandlePacketOutShutdownNode(), PacketOutShutdownNode.class)
                .registerHandler(new PacketOutSyncService().getPacketUUID(), new HandlePacketOutSyncService(), PacketOutSyncService.class)
                .registerHandler(new PacketOutSendCommand().getPacketUUID(), new HandlePacketOutSendCommand(), PacketOutSendCommand.class);
        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "der '§fNetty-Client§r' wurde erfolgreich verbunden '§f"+config.getManagerAddress()+"~"+config.getNetworkingCommunication()+"§r' angebunden", "the '§fNetty-client§r' was successfully connected '§f"+config.getManagerAddress()+"~"+config.getNetworkingCommunication()+"§r'");

    }
}
