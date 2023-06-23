package eu.metacloudservice.manager;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.events.EventDriver;
import eu.metacloudservice.manager.cloudservices.CloudServiceDriver;
import eu.metacloudservice.manager.cloudservices.entry.TaskedService;
import eu.metacloudservice.manager.cloudservices.queue.QueueDriver;
import eu.metacloudservice.manager.commands.*;
import eu.metacloudservice.manager.networking.node.HandlePacketInAuthNode;
import eu.metacloudservice.manager.networking.node.HandlePacketInNodeActionSuccess;
import eu.metacloudservice.manager.networking.node.HandlePacketInSendConsole;
import eu.metacloudservice.manager.networking.node.HandlePacketInShutdownNode;
import eu.metacloudservice.manager.networking.service.*;
import eu.metacloudservice.manager.networking.service.playerbased.HandlePacketInPlayerConnect;
import eu.metacloudservice.manager.networking.service.playerbased.HandlePacketInPlayerDisconnect;
import eu.metacloudservice.manager.networking.service.playerbased.HandlePacketInPlayerSwitchService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.node.PacketInAuthNode;
import eu.metacloudservice.networking.in.node.PacketInNodeActionSuccess;
import eu.metacloudservice.networking.in.node.PacketInSendConsole;
import eu.metacloudservice.networking.in.node.PacketInShutdownNode;
import eu.metacloudservice.networking.in.service.PacketInServiceConnect;
import eu.metacloudservice.networking.in.service.PacketInServiceDisconnect;
import eu.metacloudservice.networking.in.service.PacketInServiceReaction;
import eu.metacloudservice.networking.in.service.cloudapi.*;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerConnect;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerDisconnect;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerSwitchService;
import eu.metacloudservice.networking.in.service.playerbased.apibased.*;
import eu.metacloudservice.networking.out.node.*;
import eu.metacloudservice.networking.out.service.PacketOutServiceConnected;
import eu.metacloudservice.networking.out.service.PacketOutServiceDisconnected;
import eu.metacloudservice.networking.out.service.PacketOutServicePrepared;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerConnect;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerDisconnect;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerSwitchService;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerActionBar;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerKick;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerTitle;
import eu.metacloudservice.networking.server.NettyServer;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.Addresses;
import eu.metacloudservice.webserver.dummys.GroupList;
import eu.metacloudservice.webserver.dummys.PlayerGeneral;
import eu.metacloudservice.webserver.dummys.WhiteList;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServiceList;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

public class CloudManager {

    public static CloudServiceDriver serviceDriver;
    public static QueueDriver queueDriver;
    public static RestDriver restDriver;
    public static ManagerConfig config;
    public static boolean shutdown;

    public CloudManager() {

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Es wird versucht, den '§fCloud Manager§r' zu starten",
                "An attempt is made to start the '§fcloud manager§r'");

        System.setProperty("io.netty.noPreferDirect", "true");
        System.setProperty("client.encoding.override", "UTF-8");
        System.setProperty("io.netty.maxDirectMemory", "0");
        System.setProperty("io.netty.leakDetectionLevel", "DISABLED");
        System.setProperty("io.netty.recycler.maxCapacity", "0");
        System.setProperty("io.netty.recycler.maxCapacity.default", "0");
        if (!new File("./modules/").exists()){
            new File("./modules/").mkdirs();
            Driver.getInstance().getMessageStorage().packetLoader.loadModules();
        }



        new File("./local/GLOBAL/EVERY/plugins/").mkdirs();
        new File("./local/GLOBAL/EVERY_SERVER/plugins/").mkdirs();
        new File("./local/GLOBAL/EVERY_PROXY/plugins/").mkdirs();
        new File("./local/groups/").mkdirs();
        new File( "./local/templates/").mkdirs();
        config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        if (!new File("./connection.key").exists()){
            AuthenticatorKey key = new AuthenticatorKey();
            String  k = Driver.getInstance().getMessageStorage().utf8ToUBase64(UUID.randomUUID() + UUID.randomUUID().toString()+ UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID());
            key.setKey(k);
            new ConfigDriver("./connection.key").save(key);
        }
        Driver.getInstance().getMessageStorage().canUseMemory = config.getCanUsedMemory() -250;
        System.setProperty("log4j.configurationFile", "log4j2.properties");
        initNetty(config);
        restDriver = new RestDriver(config.getManagerAddress(), config.getRestApiCommunication());
        initRestService();

        if (!new File("./local/messages.json").exists()){
            new ConfigDriver("./local/messages.json").save(new Messages("§8▷ §bMetaCloud §8▌ §7",
                    "%PREFIX%Successfully connected to §a%SERVICE%",
                    "%PREFIX%§cthe service is unfortunately full",
                    "%PREFIX%§cYou are already on a Fallback",
                    "%PREFIX%§cthe group is in maintenance",
                    "%PREFIX%§cCould not find a suitable fallback to connect you to!",
                    "§8▷ §cthe network is full buy the premium to be able to despite that on it",
                    "§8▷ §cthe network is currently undergoing maintenance",
                    "§8▷ §cThe server you were on went down, but no fallback server was found!",
                    "§8▷ §cpleas connect over the main proxy",
                    "§8▷ §cYou are already on the Network"));
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
        }


        if (!new File("./local/GLOBAL/EVERY/plugins/metacloud-api.jar").exists()){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Versuche die Datei '§fmetacloud-api.jar§r' herunter zuladen",
                    "Try to download the file '§fmetacloud-api.jar§r'.");
            Driver.getInstance().getMessageStorage().packetLoader.loadAPI();
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,"Der download war erfolgreich",
                    "The download was successful");
        }

        Driver.getInstance().getModuleDriver().load();

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wird versucht, alle Befehle zu laden und ihre Bereitstellung deutlich zu machen",
                "It is tried to load all commands and to make the provision of them clear");
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new HelpCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new GroupCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new ClearCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new StopCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new ServiceCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new NodeCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new ReloadCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new MetaCloudCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new QueueCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new PlayersCommand());

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wurden '§f"+Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().size()+" Befehle§r' gefunden und geladen",
                "There were '§f"+Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().size()+" commands§r' found and loaded");

        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Die Cloud erfolgreich gestartet ist, können Sie sie von nun an mit '§fhelp§r' nutzen.",
                "The cloud is successfully started, you can use it from now on with '§fhelp§r'.");
        queueDriver= new QueueDriver();

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
                Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getKickOnlyProxyJoin()),
                Driver.getInstance().getMessageStorage().utf8ToUBase64(raw.getKickAlreadyOnNetwork()));

        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/message/default", new ConfigDriver().convert(msg)));
        GroupList groupList = new GroupList();
        groupList.setGroups(Driver.getInstance().getGroupDriver().getAllStrings());
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/cloudgroup/general", new ConfigDriver().convert(groupList)));
        Driver.getInstance().getGroupDriver().getAll().forEach(group -> {
            if (Driver.getInstance().getWebServer().getRoute("/cloudgroup/" +group.getGroup()) == null){
                Driver.getInstance().getWebServer().addRoute(new RouteEntry("/cloudgroup/" + group.getGroup(), new ConfigDriver().convert(group)));

            }else {
                Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + group.getGroup(), new ConfigDriver().convert(group));
            }
        });

        LiveServiceList liveGroup = new LiveServiceList();
        liveGroup.setCloudServiceSplitter(config.getSplitter());
        liveGroup.setCloudServices(new ArrayList<>());
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/cloudservice/general", new ConfigDriver().convert(liveGroup)));
        WhiteList whitelistConfig = new WhiteList();
        whitelistConfig.setWhitelist(config.getWhitelist());
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/default/whitelist", new ConfigDriver().convert(whitelistConfig)));
        PlayerGeneral general = new PlayerGeneral();
        general.setCloudplayers(new ArrayList<>());
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/cloudplayer/genernal", new ConfigDriver().convert(general)));

        Addresses AddressesConfig = new Addresses();

        ArrayList<String> addresses = new ArrayList<>();
        config.getNodes().forEach(managerConfigNodes -> {
            addresses.add(managerConfigNodes.getAddress());
        });
        AddressesConfig.setWhitelist(addresses);
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/default/addresses", new ConfigDriver().convert(AddressesConfig)));
        serviceDriver = new CloudServiceDriver();
    }

    public void initNetty(ManagerConfig config){
        new NettyDriver();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Netty-Server wird vorbereitet und dann gestartet", "The Netty server is prepared and then started");

        NettyDriver.getInstance().whitelist.add("127.0.0.1");

        config.getNodes().forEach(managerConfigNodes -> {
            if (!NettyDriver.getInstance().whitelist.contains(managerConfigNodes.getAddress())){
                NettyDriver.getInstance().whitelist.add(managerConfigNodes.getAddress());
            }
        });

        /*
         * this starts a new NettyServer with Epoll on EpollEventLoopGroup or NioEventLoopGroup basis.
         * */

        NettyDriver.getInstance().nettyServer = new NettyServer();
        NettyDriver.getInstance().nettyServer.bind( config.getNetworkingCommunication()).start();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der '§fNetty-Server§r' wurde erfolgreich an Port '§f"+config.getNetworkingCommunication()+"§r' angebunden", "The '§fNetty-server§r' was successfully bound on port '§f"+config.getNetworkingCommunication()+"§r'");

        //PACKETS
        NettyDriver.getInstance().packetDriver
                /*
                * in this part all packages and traders sent to the server are registered
                * {@link NettyAdaptor} handles the packet and looks where it belongs
                * {@link Packet} handles the packets are written and read via a ByteBuf
                 * */
                //NODE
                .registerHandler(new PacketInAuthNode().getPacketUUID(), new HandlePacketInAuthNode(), PacketInAuthNode.class)
                .registerHandler(new PacketInNodeActionSuccess().getPacketUUID(), new HandlePacketInNodeActionSuccess(), PacketInNodeActionSuccess.class)
                .registerHandler(new PacketInShutdownNode().getPacketUUID(), new HandlePacketInShutdownNode(), PacketInShutdownNode.class)
                .registerHandler(new PacketInSendConsole().getPacketUUID(), new HandlePacketInSendConsole(), PacketInSendConsole.class)
                //API
                .registerHandler(new PacketInServiceConnect().getPacketUUID(), new HandlePacketInServiceConnect(), PacketInServiceConnect.class)
                .registerHandler(new PacketInServiceDisconnect().getPacketUUID(), new HandlePacketInServiceDisconnect(), PacketInServiceDisconnect.class)
                .registerHandler(new PacketInChangeState().getPacketUUID(), new HandlePacketInChangeState(), PacketInChangeState.class)
                .registerHandler(new PacketInDispatchCommand().getPacketUUID(), new HandlePacketInDispatchCommand(), PacketInDispatchCommand.class)
                .registerHandler(new PacketInDispatchMainCommand().getPacketUUID(), new HandlePacketInDispatchMainCommand(), PacketInDispatchMainCommand.class)
                .registerHandler(new PacketInLaunchService().getPacketUUID(), new HandlePacketInLaunchService(), PacketInLaunchService.class)
                .registerHandler(new PacketInStopGroup().getPacketUUID(), new HandlePacketInStopGroup(), PacketInStopGroup.class)
                .registerHandler(new PacketInStopService().getPacketUUID(), new HandlePacketInStopService(), PacketInStopService.class)
                .registerHandler(new PacketInAPIPlayerMessage().getPacketUUID(), new HandlePacketInAPIPlayerMessage(), PacketInAPIPlayerMessage.class)
                .registerHandler(new PacketInAPIPlayerConnect().getPacketUUID(), new HandlePacketInAPIPlayerConnect(), PacketInAPIPlayerConnect.class)
                .registerHandler(new PacketInAPIPlayerKick().getPacketUUID(), new HandlePacketInAPIPlayerKick(), PacketInAPIPlayerKick.class)
                .registerHandler(new PacketInAPIPlayerTitle().getPacketUUID(), new HandlePacketInAPIPlayerTitle(), PacketInAPIPlayerTitle.class)
                .registerHandler(new PacketInAPIPlayerActionBar().getPacketUUID(), new HandlePacketInAPIPlayerActionBar(), PacketInAPIPlayerActionBar.class)
                .registerHandler(new PacketInAPIPlayerTab().getPacketUUID(), new HandlePacketInAPIPlayerTab(), PacketInAPIPlayerTab.class)
                .registerHandler(new PacketInCloudPlayerComponent().getPacketUUID(), new HandlePacketInCloudPlayerComponent(), PacketInCloudPlayerComponent.class)
                .registerHandler(new PacketOutAPIPlayerDispactchCommand().getPacketUUID(), new HandlePacketOutAPIPlayerDispatchCommand(), PacketOutAPIPlayerDispactchCommand.class)

                //PLAYER
                .registerHandler(new PacketInPlayerConnect().getPacketUUID(), new HandlePacketInPlayerConnect(), PacketInPlayerConnect.class)
                .registerHandler(new PacketInPlayerDisconnect().getPacketUUID(), new HandlePacketInPlayerDisconnect(), PacketInPlayerDisconnect.class)
                .registerHandler(new PacketInPlayerSwitchService().getPacketUUID(), new HandlePacketInPlayerSwitchService(), PacketInPlayerSwitchService.class);

    }
    public void initRestService(){
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wird versucht, den Webserver zu laden und zu starten",
                "An attempt is made to load the web server and start it");
        Driver.getInstance().runWebServer();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Webserver wurde §ferfolgreich§r geladen und gestartet auf '§f" + config.getRestApiCommunication() +"§r'", "The web server is §fsuccessfully§r loaded and started on '§f" + config.getRestApiCommunication() +"§r'");
    }

    public static void shutdownHook(){
        shutdown = true;
        NettyDriver.getInstance().packetDriver.getAdaptor().clear();
        serviceDriver.getServicesFromNode("InternalNode").forEach(TaskedService::handelQuit);
        Driver.getInstance().getModuleDriver().unload();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Driver.getInstance().getWebServer().close();
        NettyDriver.getInstance().nettyServer.close();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Danke für die Nutzung von MetaCloud ;->",
                "Thank you for using MetaCloud ;->");
        System.exit(0);
    }
}
