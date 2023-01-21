package eu.themetacloudservice.manager;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.manager.cloudservices.CloudServiceDriver;
import eu.themetacloudservice.manager.commands.ClearCommand;
import eu.themetacloudservice.manager.commands.GroupCommand;
import eu.themetacloudservice.manager.commands.HelpCommand;
import eu.themetacloudservice.manager.commands.StopCommand;
import eu.themetacloudservice.manager.networking.ManagerNetworkChannel;
import eu.themetacloudservice.network.autentic.PackageAuthenticByManager;
import eu.themetacloudservice.network.autentic.PackageAuthenticRequestFromManager;
import eu.themetacloudservice.network.autentic.PackageCallBackAuthenticByManager;
import eu.themetacloudservice.network.tasks.PackageLaunchService;
import eu.themetacloudservice.network.tasks.PackageStopNodes;
import eu.themetacloudservice.network.tasks.PackageStopTask;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.server.NettyServer;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.webserver.RestDriver;
import eu.themetacloudservice.webserver.WebServer;
import eu.themetacloudservice.webserver.entry.RouteEntry;

import java.io.File;
import java.util.UUID;

public class CloudManager {

    public static CloudServiceDriver serviceDriver;

    public CloudManager() {
        serviceDriver = new CloudServiceDriver();
        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        System.setProperty("log4j.configurationFile", "log4j2.properties");
        initNetty(config);
        if (!new File("./connection.key").exists()){
            AuthenticatorKey key = new AuthenticatorKey();
            String  k = Driver.getInstance().getMessageStorage().utf8ToUBase64(UUID.randomUUID() + UUID.randomUUID().toString()+ UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID());
            key.setKey(k);
            new ConfigDriver("./connection.key").save(key);
        }
        if (!new File("./local/server-icon.png").exists()){
            Driver.getInstance().getMessageStorage().packetLoader.loadLogo();
        }


        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new HelpCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new GroupCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new ClearCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new StopCommand());
        Boolean modules =   new File("./modules/").mkdirs();
        Boolean global =  new File("./local/GLOBAL/").mkdirs();
        Boolean groups = new File("./local/groups/").mkdirs();
        Boolean templates = new File("./local/templates/").mkdirs();
        Driver.getInstance().getModuleDriver().loadAllModules();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFORMATION, "die Cloud erfolgreich gestartet ist, können Sie sie von nun an mit '§fhelp§r' nutzen.",
                "the cloud is successfully started, you can use it from now on with '§fhelp§r'.");
        Driver.getInstance().runWebServer();
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/test", "{\"test\":\"dies ist ein Test um zuschauen ob alles geht\"}"));
        new RestDriver().put("/test", "{\"test\":\"dies ist das update\"}");

        Driver.getInstance().getTerminalDriver().log(Type.INFORMATION, new RestDriver().get("/test"));
    }


    public void initNetty(ManagerConfig config){
        new NettyDriver();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "der Netty-Server wird vorbereitet und dann gestartet", "the Netty server is prepared and then started");
        NettyDriver.getInstance().nettyServer = new NettyServer();
        NettyDriver.getInstance().nettyServer.bind(config.getNetworkingCommunication()).start();

        //PACKETS
        NettyDriver.getInstance().packetDriver
                .handelPacket(PackageLaunchService.class)
                .handelPacket(PackageStopTask.class)
                .handelPacket(PackageAuthenticByManager.class)
                .handelPacket(PackageCallBackAuthenticByManager.class)
                .handelPacket(PackageAuthenticRequestFromManager.class)
                .handelPacket(PackageStopNodes.class)
                .handelListener(new ManagerNetworkChannel());
        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "der '§fNetty-Server§r' wurde erfolgreich an Port '§f"+config.getNetworkingCommunication()+"§r' angebunden", "the '§fNetty-server§r' was successfully bound on port '§f"+config.getNetworkingCommunication()+"§r'");
    }


    public static void shutdownHook(){


        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "testdown", "test down");
        NettyDriver.getInstance().nettyServer.sendToAllPackets(new PackageStopNodes());
        NettyDriver.getInstance().nettyServer.close();

        System.exit(0);
    }
}
