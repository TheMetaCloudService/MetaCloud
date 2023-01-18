package eu.themetacloudservice.node;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.network.autentic.PackageAuthenticByManager;
import eu.themetacloudservice.network.autentic.PackageAuthenticRequestFromManager;
import eu.themetacloudservice.network.autentic.PackageCallBackAuthenticByManager;
import eu.themetacloudservice.network.tasks.PackageLaunchService;
import eu.themetacloudservice.network.tasks.PackageStopNodes;
import eu.themetacloudservice.network.tasks.PackageStopTask;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.client.NettyClient;
import eu.themetacloudservice.node.commands.HelpCommand;
import eu.themetacloudservice.node.commands.StopCommand;
import eu.themetacloudservice.node.networking.NodeNetworkChannel;
import eu.themetacloudservice.terminal.enums.Type;

import java.io.File;

public class CloudNode {

    public CloudNode() {
        NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
        System.setProperty("log4j.configurationFile", "log4j2.properties");
        if (!new File("./connection.key").exists()){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "die '§fconnection.key§r' config wurde nicht gefunden!", "the '§fconnection.key§r' config was not found!");

            System.exit(0);
        }

        if (!new File("./local/server-icon.png").exists()){
            Driver.getInstance().getMessageStorage().packetLoader.loadLogo();
        }
        initNetty(config);

        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new HelpCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new StopCommand());
        new File("./modules/").mkdirs();
        new File("./local/GLOBAL/").mkdirs();
        new File("./local/templates/").mkdirs();

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFORMATION, "die Cloud erfolgreich gestartet ist, können Sie sie von nun an mit '§fhelp§r' nutzen.",
                "the cloud is successfully started, you can use it from now on with '§fhelp§r'.");


    }


    private void initNetty(NodeConfig config){
        new NettyDriver();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "der Netty-Client wird vorbereitet und dann gestartet", "the Netty client is prepared and then started");

        NettyDriver.getInstance().nettyClient = new NettyClient();
        NettyDriver.getInstance().nettyClient.bind(config.getManagerAddress(), config.getNetworkingCommunication()).connect();

        NettyDriver.getInstance().packetDriver
                .handelPacket(PackageLaunchService.class)
                .handelPacket(PackageStopTask.class)
                .handelPacket(PackageAuthenticByManager.class)
                .handelPacket(PackageCallBackAuthenticByManager.class)
                .handelPacket(PackageAuthenticRequestFromManager.class)
                .handelPacket(PackageStopNodes.class)
                .handelListener(new NodeNetworkChannel());


        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "der '§fNetty-Client§r' wurde erfolgreich verbunden '§f"+config.getManagerAddress()+"~"+config.getNetworkingCommunication()+"§r' angebunden", "the '§fNetty-client§r' was successfully connected '§f"+config.getManagerAddress()+"~"+config.getNetworkingCommunication()+"§r'");

    }
}
