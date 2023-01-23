package eu.themetacloudservice.manager;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.manager.cloudservices.CloudServiceDriver;
import eu.themetacloudservice.manager.cloudservices.entry.TaskedService;
import eu.themetacloudservice.manager.cloudservices.queue.QueueDriver;
import eu.themetacloudservice.manager.commands.*;
import eu.themetacloudservice.manager.networking.ManagerNetworkChannel;
import eu.themetacloudservice.network.autentic.PackageAuthenticByManager;
import eu.themetacloudservice.network.autentic.PackageAuthenticRequestFromManager;
import eu.themetacloudservice.network.autentic.PackageCallBackAuthenticByManager;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceExit;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceLaunch;
import eu.themetacloudservice.network.nodes.from.PackageToManagerHandelNodeShutdown;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelKillNode;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceExit;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceLaunch;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelSync;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.server.NettyServer;
import eu.themetacloudservice.terminal.enums.Type;


import java.io.File;
import java.util.UUID;

public class CloudManager {

    public static CloudServiceDriver serviceDriver;
    public static QueueDriver queueDriver;
    public static boolean shutdown;

    public CloudManager() {
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Es wird versucht, den '§fCloud Manager§r' zu starten",
                "an attempt is made to start the '§fcloud manager§r'");
        new File("./modules/").mkdirs();
        new File("./local/GLOBAL/").mkdirs();
        new File("./local/groups/").mkdirs();
        new File( "./local/templates/").mkdirs();
        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        Driver.getInstance().getMessageStorage().canUseMemory = config.getCanUsedMemory();
        System.setProperty("log4j.configurationFile", "log4j2.properties");
        initNetty(config);

        if (!new File("./connection.key").exists()){
            AuthenticatorKey key = new AuthenticatorKey();
            String  k = Driver.getInstance().getMessageStorage().utf8ToUBase64(UUID.randomUUID() + UUID.randomUUID().toString()+ UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID());
            key.setKey(k);
            new ConfigDriver("./connection.key").save(key);
        }

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Die Datei '§fconnection.key§r' wurde geladen",
                "the '§fconnection.key§r' file was loaded");

        if (!new File("./local/server-icon.png").exists()){
            Driver.getInstance().getMessageStorage().packetLoader.loadLogo();
        }
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Die Datei '§fserver-icon.png§r' wurde gefunden",
                "the '§fserver-icon.png§r' file was found");

        Driver.getInstance().getModuleDriver().loadAllModules();
        initRestService();

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wird versucht, alle Befehle zu laden und ihre Bereitstellung deutlich zu machen",
                "it is tried to load all commands and to make the provision of them clear");

        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new HelpCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new GroupCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new ClearCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new StopCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new ServiceCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new NodeCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new MetaCloudCommand());

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wurden '§f"+Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().size()+" Befehle§r' gefunden und geladen",
                "there were '§f"+Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().size()+" commands§r'  found and loaded");

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Die Cloud erfolgreich gestartet ist, können Sie sie von nun an mit '§fhelp§r' nutzen.",
                "the cloud is successfully started, you can use it from now on with '§fhelp§r'.");
        queueDriver= new QueueDriver();
        queueDriver.handler();
        serviceDriver = new CloudServiceDriver();

    }


    public void initNetty(ManagerConfig config){
        new NettyDriver();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Netty-Server wird vorbereitet und dann gestartet", "the Netty server is prepared and then started");
        NettyDriver.getInstance().nettyServer = new NettyServer();
        NettyDriver.getInstance().nettyServer.bind(config.getNetworkingCommunication()).start();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der '§fNetty-Server§r' wurde erfolgreich an Port '§f"+config.getNetworkingCommunication()+"§r' angebunden", "the '§fNetty-server§r' was successfully bound on port '§f"+config.getNetworkingCommunication()+"§r'");

        //PACKETS
        NettyDriver.getInstance().packetDriver
                .handelPacket(PackageAuthenticByManager.class)
                .handelPacket(PackageCallBackAuthenticByManager.class)
                .handelPacket(PackageAuthenticRequestFromManager.class)
                .handelPacket(PackageToNodeHandelSync.class)
                .handelPacket(PackageToNodeHandelServiceExit.class)
                .handelPacket(PackageToNodeHandelServiceLaunch.class)
                .handelPacket(PackageToManagerHandelNodeShutdown.class)
                .handelPacket(PackageToManagerCallBackServiceExit.class)
                .handelPacket(PackageToManagerCallBackServiceLaunch.class)
                .handelListener(new ManagerNetworkChannel());
    }


    public void initRestService(){
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wird versucht, den Webserver zu laden und zu starten",
                "an attempt is made to load the web server and start it");
        Driver.getInstance().runWebServer();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Webserver wurde §ferfolgreich§r geladen und gestartet", "the web server is §fsuccessfully§r loaded and started");
    }

    public static void shutdownHook(){
        shutdown = true;

        serviceDriver.getServicesFromNode("InternalNode").forEach(TaskedService::handelQuit);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Driver.getInstance().getWebServer().close();
        NettyDriver.getInstance().nettyServer.sendToAllPackets(new PackageToNodeHandelKillNode());
        NettyDriver.getInstance().nettyServer.close();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Danke für die Nutzung von MetaCloud ;->",
                "thank you for using MetaCloud ;->");
        System.exit(0);
    }
}
