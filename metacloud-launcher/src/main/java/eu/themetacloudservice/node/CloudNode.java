package eu.themetacloudservice.node;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.network.autentic.PackageAuthenticByManager;
import eu.themetacloudservice.network.autentic.PackageAuthenticRequestFromManager;
import eu.themetacloudservice.network.autentic.PackageCallBackAuthenticByManager;
import eu.themetacloudservice.network.cloudcommand.*;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerChangeService;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerConnect;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerDisconnect;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceExit;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceLaunch;
import eu.themetacloudservice.network.nodes.from.PackageToManagerHandelNodeShutdown;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelKillNode;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceExit;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceLaunch;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelSync;
import eu.themetacloudservice.network.service.*;
import eu.themetacloudservice.network.service.proxyconnect.PackageConnectedProxyCallBack;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.client.NettyClient;
import eu.themetacloudservice.node.cloudservices.CloudServiceDriver;
import eu.themetacloudservice.node.commands.HelpCommand;
import eu.themetacloudservice.node.commands.StopCommand;
import eu.themetacloudservice.node.networking.NodeAuthChannel;
import eu.themetacloudservice.node.networking.NodeHandelKillChannel;
import eu.themetacloudservice.node.networking.NodeHandelServicesChannel;
import eu.themetacloudservice.terminal.enums.Type;

import java.io.File;

public class CloudNode {

    public  static  CloudServiceDriver cloudServiceDriver;

    public CloudNode() {
        new File("./modules/").mkdirs();
        new File("./local/GLOBAL/").mkdirs();
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
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Die Datei '§fconnection.key§r' wurde geladen",
                "the '§fconnection.key§r' file was loaded");

        if (!new File("./local/server-icon.png").exists()){
            Driver.getInstance().getMessageStorage().packetLoader.loadLogo();
        }
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Die Datei '§fserver-icon.png§r' wurde gefunden",
                "the '§fserver-icon.png§r' file was found");
        initNetty(config);
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wird versucht, alle Befehle zu laden und ihre Bereitstellung deutlich zu machen",
                "it is tried to load all commands and to make the provision of them clear");
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new HelpCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new StopCommand());


        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wurden '§f"+Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().size()+" Befehle§r' gefunden und geladen",
                "there were '§f"+Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().size()+" commands§r'  found and loaded");

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Die Cloud erfolgreich gestartet ist, können Sie sie von nun an mit '§fhelp§r' nutzen.",
                "the cloud is successfully started, you can use it from now on with '§fhelp§r'.");


    }


    public static void shutdownHook(){

        NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);


        cloudServiceDriver.shutdownALLFORCE();

        NettyDriver.getInstance().nettyClient.sendPacket(new PackageToManagerHandelNodeShutdown(config.getNodeName()));
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
                //NODE PACKAGE
                .handelPacket(PackageAuthenticByManager.class)
                .handelPacket(PackageCallBackAuthenticByManager.class)
                .handelPacket(PackageAuthenticRequestFromManager.class)
                .handelPacket(PackageToNodeHandelSync.class)
                .handelPacket(PackageToNodeHandelServiceExit.class)
                .handelPacket(PackageToNodeHandelServiceLaunch.class)
                .handelPacket(PackageToManagerHandelNodeShutdown.class)
                .handelPacket(PackageToManagerCallBackServiceExit.class)
                //EVENT PACKET
                .handelPacket(PackageConnectedServiceToALL.class)
                .handelPacket(PackageRegisterServiceToALL.class)
                .handelPacket(PackageShutdownServiceToALL.class)
                .handelPacket(PackageLaunchServiceToALL.class)
                .handelPacket(PackageConnectedProxyCallBack.class)
                //SERVICE PACKAGE
                .handelPacket(PackageRunCommand.class)
                .handelPacket(PackageServiceShutdown.class)
                .handelPacket(PackageToManagerCallBackServiceLaunch.class)
                .handelPacket(PackageCloudPlayerConnect.class)
                .handelPacket(PackageCloudPlayerChangeService.class)
                .handelPacket(PackageCloudPlayerDisconnect.class)
                // CLOUD COMMAND PACKETS
                .handelPacket(PackageCloudCommandEXIT.class)
                .handelPacket(PackageCloudCommandMAINTENANCE.class)
                .handelPacket(PackageCloudCommandPLAYERS.class)
                .handelPacket(PackageCloudCommandRELOAD.class)
                .handelPacket(PackageCloudCommandRUN.class)
                .handelPacket(PackageCloudCommandSTOP.class)
                .handelPacket(PackageCloudCommandSTOPGROUP.class)
                .handelPacket(PackageCloudCommandSYNC.class)
                .handelPacket(PackageCloudCommandWITELIST.class)
                .handelListener(new NodeHandelServicesChannel())
                .handelListener(new NodeHandelKillChannel())
                .handelListener(new NodeAuthChannel());


        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "der '§fNetty-Client§r' wurde erfolgreich verbunden '§f"+config.getManagerAddress()+"~"+config.getNetworkingCommunication()+"§r' angebunden", "the '§fNetty-client§r' was successfully connected '§f"+config.getManagerAddress()+"~"+config.getNetworkingCommunication()+"§r'");

    }
}
