package eu.themetacloudservice.manager.commands;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.message.Messages;
import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;
import eu.themetacloudservice.terminal.commands.CommandAdapter;
import eu.themetacloudservice.terminal.commands.CommandInfo;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.terminal.utils.TerminalStorageLine;
import eu.themetacloudservice.webserver.dummys.WhitelistConfig;
import eu.themetacloudservice.webserver.entry.RouteEntry;

import java.util.ArrayList;

@CommandInfo(command = "reload", DEdescription = "lade alle module sowie weiter daten neu", ENdescription = "reload all modules and further data", aliases = {"rl"})
public class ReloadCommand extends CommandAdapter {


    @Override
    public void performCommand(CommandAdapter command, String[] args) {

        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND, "Die Cloud wurde mit Erfolg neu geladen", "The cloud was reloaded with success");
        Driver.getInstance().getModuleDriver().reloadAllModules();
        Messages raw = (Messages) new ConfigDriver("./local/messages.json").read(Messages.class);
        Messages msg = new Messages(Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getPrefix()),
                Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getSuccessfullyConnected()),
                Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getServiceIsFull()),
                Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getAlreadyOnFallback()),
                Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getConnectingGroupMaintenance()),
                Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getNoFallbackServer()),
                Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getKickNetworkIsFull()),
                Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getKickNetworkIsMaintenance()),
                Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getKickNoFallback()),
                Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getKickOnlyProxyJoin()));


        WhitelistConfig whitelistConfig = new WhitelistConfig();
        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        whitelistConfig.setWhitelist(config.getWhitelist());
        Driver.getInstance().getWebServer().updateRoute("/whitelist", new ConfigDriver().convert(whitelistConfig));


        Driver.getInstance().getWebServer().updateRoute("/messages", new ConfigDriver().convert(msg));
        try {
            Driver.getInstance().getGroupDriver().getAll().forEach(group -> {
                if (Driver.getInstance().getWebServer().getRoute("/"+group.getGroup()) == null){
                    Driver.getInstance().getWebServer().addRoute(new RouteEntry("/" + group.getGroup(), new ConfigDriver().convert(group)));
                }else {
                    Driver.getInstance().getWebServer().updateRoute("/" + group.getGroup(), new ConfigDriver().convert(group));
                }
            });
        }catch (Exception ignored){}


    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
