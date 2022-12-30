package eu.themetacloudservice.manager;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.manager.commands.GroupCommand;
import eu.themetacloudservice.manager.commands.HelpCommand;

import java.io.File;
import java.util.UUID;

public class CloudManager {


    public CloudManager(){

        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
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

        new File("./modules/").mkdirs();
        new File("./local/GLOBAL/").mkdirs();
        new File("./local/groups/").mkdirs();
        new File("./local/templates/").mkdirs();
        Driver.getInstance().getWebDriver().hostWebServer(new WebBuilder()
                .bind(config.getRestApiCommunication())
                .handelConnect(Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey())));

        //todo: make the Networking finish and connect it

        //todo: make an autostart for the Groups with an Queue
    }
}
