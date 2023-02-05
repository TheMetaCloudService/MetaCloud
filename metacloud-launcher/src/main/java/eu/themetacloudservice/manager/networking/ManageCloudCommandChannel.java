package eu.themetacloudservice.manager.networking;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.message.Messages;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.network.cloudcommand.*;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.listeners.IPacketListener;
import eu.themetacloudservice.webserver.dummys.WhitelistConfig;
import eu.themetacloudservice.webserver.entry.RouteEntry;
import io.netty.channel.ChannelHandlerContext;

public class ManageCloudCommandChannel implements IPacketListener {
    @Override
    public void onReceive(ChannelHandlerContext paramChannelHandlerContext, Packet paramPacket) {

        if (paramPacket instanceof PackageCloudCommandRELOAD){
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

        }if (paramPacket instanceof PackageCloudCommandEXIT){
            CloudManager.shutdownHook();
        }
        if (paramPacket instanceof PackageCloudCommandSTOP){
            CloudManager.serviceDriver.unregister(((PackageCloudCommandSTOP) paramPacket).getService());
        }

        if (paramPacket instanceof PackageCloudCommandMAINTENANCE){
            Group group = Driver.getInstance().getGroupDriver().load(((PackageCloudCommandMAINTENANCE) paramPacket).getGroup());

            if (group.isMaintenance()){
                group.setMaintenance(false);
            }else {
                group.setMaintenance(true);
            }
            Driver.getInstance().getGroupDriver().update(group.getGroup(), group);
            Driver.getInstance().getWebServer().updateRoute("/" + group.getGroup(), new ConfigDriver().convert(group));
        }

        if (paramPacket instanceof PackageCloudCommandWITELIST){


            if (((PackageCloudCommandWITELIST) paramPacket).isAddTO()){
                Driver.getInstance().getTerminalDriver().getCommandDriver().executeCommand("service whitelist add " + ((PackageCloudCommandWITELIST) paramPacket).getName());
            }else {
                Driver.getInstance().getTerminalDriver().getCommandDriver().executeCommand("service whitelist remove " + ((PackageCloudCommandWITELIST) paramPacket).getName());
            }

        }
        if (paramPacket instanceof PackageCloudCommandRUN){
            Driver.getInstance().getTerminalDriver().getCommandDriver().executeCommand("service run " + ((PackageCloudCommandRUN) paramPacket).getGroup() + " " + ((PackageCloudCommandRUN) paramPacket).getAmount());
        }if (paramPacket instanceof PackageCloudCommandSTOPGROUP){
            Driver.getInstance().getTerminalDriver().getCommandDriver().executeCommand("service stopgroup " + ((PackageCloudCommandSTOPGROUP) paramPacket).getGroup());
        }if (paramPacket instanceof PackageCloudCommandPLAYERS){

            Driver.getInstance().getTerminalDriver().getCommandDriver().executeCommand("group "+((PackageCloudCommandPLAYERS) paramPacket).getGroup()+" setminamount " + ((PackageCloudCommandPLAYERS) paramPacket).getPlayers());

        }if (paramPacket instanceof PackageCloudCommandSYNC){
            CloudManager.serviceDriver.getService(((PackageCloudCommandSYNC) paramPacket).getService()).handelSync();
        }
    }

    @Override
    public void onConnect(ChannelHandlerContext paramChannelHandlerContext) {

    }

    @Override
    public void onDisconnect(ChannelHandlerContext paramChannelHandlerContext) {

    }
}
