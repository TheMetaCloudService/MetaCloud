package eu.themetacloudservice.manager;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.manager.commands.ClearCommand;
import eu.themetacloudservice.manager.commands.GroupCommand;
import eu.themetacloudservice.manager.commands.HelpCommand;
import eu.themetacloudservice.manager.commands.StopCommand;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.server.NettyServer;
import eu.themetacloudservice.terminal.enums.Type;
import io.netty.util.ResourceLeakDetector;


import java.io.File;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CloudManager {


    public CloudManager(){

        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        System.setProperty("log4j.configurationFile", "log4j2.properties");
        initNetty(config);

        if (!new File("./connection.key").exists()){
            AuthenticatorKey key = new AuthenticatorKey();
            String  k = Driver.getInstance().getMessageStorage().utf8ToUBase64(UUID.randomUUID().toString() + UUID.randomUUID().toString()+ UUID.randomUUID().toString()+ UUID.randomUUID().toString()+ UUID.randomUUID().toString()+ UUID.randomUUID().toString()+ UUID.randomUUID().toString()+ UUID.randomUUID().toString()+ UUID.randomUUID().toString());
            key.setKey(k);
            new ConfigDriver("./connection.key").save(key);
        }
        if (!new File("./local/server-icon.png").exists()){
            Driver.getInstance().getMessageStorage().packetLoader.loadLogo();
        }
        AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new HelpCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new GroupCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new ClearCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new StopCommand());
        new File("./modules/").mkdirs();
        new File("./local/GLOBAL/").mkdirs();
        new File("./local/groups/").mkdirs();
        new File("./local/templates/").mkdirs();


        //todo: make an autostart for the Groups with an Queue

    }


    public void initNetty(ManagerConfig config){
        new NettyDriver();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "der Netty-Server wird vorbereitet und dann gestartet", "the Netty server is prepared and then started");
        NettyDriver.getInstance().nettyServer = new NettyServer();
        NettyDriver.getInstance().nettyServer.bind(config.getNetworkingCommunication()).start();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "der '§fNetty-Server§r' wurde erfolgreich an Port '§f"+config.getNetworkingCommunication()+"§r' angebunden", "the '§fNetty-server§r' was successfully bound on port '§f"+config.getNetworkingCommunication()+"§r'");

    }

    public static void shutdownHook(){
        System.exit(0);
    }
}
