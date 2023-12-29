/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.node;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.authenticator.AuthenticatorKey;
import eu.metacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.metacloudservice.networking.packet.packets.in.node.PacketInAuthNode;
import eu.metacloudservice.networking.packet.packets.in.node.PacketInShutdownNode;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.client.NettyClient;
import eu.metacloudservice.networking.packet.packets.out.node.*;
import eu.metacloudservice.node.cloudservices.CloudServiceDriver;
import eu.metacloudservice.node.commands.HelpCommand;
import eu.metacloudservice.node.commands.StopCommand;
import eu.metacloudservice.node.commands.UpdateCommand;
import eu.metacloudservice.node.networking.*;
import eu.metacloudservice.storage.IRunAble;
import eu.metacloudservice.terminal.animation.AnimationDriver;
import eu.metacloudservice.terminal.enums.Type;

import java.io.File;

public class CloudNode implements IRunAble {

    public  static  CloudServiceDriver cloudServiceDriver;
    public static NodeConfig config;

    public static void shutdownHook(){
        NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
        cloudServiceDriver.shutdownALLFORCE();
        Driver.getInstance().getModuleDriver().unload();
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInShutdownNode(config.getNodeName()));
        NettyDriver.getInstance().nettyClient.close();
        Driver.getInstance().getMessageStorage().eventDriver.clearListeners();
        Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("cloud-shutting-down"));
        System.exit(0);
    }

    private void initNetty(NodeConfig config){
        new NettyDriver();
        Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("netty-client-prepared"));
        NettyDriver.getInstance().nettyClient = new NettyClient();
         NettyDriver.getInstance().nettyClient.bind(config.getManagerAddress(), config.getNetworkingCommunication()).connect();
        NettyDriver.getInstance().getPacketDriver()
                /*
                * in this part all packages and trader sent form the server are registered
                * {@link NettyAdaptor} handles the packet and looks where it belongs
                * {@link Packet} handles the packets are written and read via a ByteBuf
                * */
                .registerHandler(new PacketOutEnableConsole().getPacketUUID(), new HandlePacketOutEnableConsole(), PacketOutEnableConsole.class)
                .registerHandler(new PacketOutDisableConsole().getPacketUUID(), new HandlePacketOutDisableConsole(), PacketOutDisableConsole.class)
                .registerHandler(new PacketOutAuthSuccess().getPacketUUID(), new HandlePacketOutAuthSuccess(), PacketOutAuthSuccess.class)
                .registerHandler(new PacketOutStopService().getPacketUUID(), new HandlePacketOutStopService(), PacketOutStopService.class)
                .registerHandler(new PacketOutLaunchService().getPacketUUID(), new HandlePacketOutLaunchService(), PacketOutLaunchService.class)
                .registerHandler(new PacketOutShutdownNode().getPacketUUID(), new HandlePacketOutShutdownNode(), PacketOutShutdownNode.class)
                .registerHandler(new PacketOutSyncService().getPacketUUID(), new HandlePacketOutSyncService(), PacketOutSyncService.class)
                .registerHandler(new PacketOutRestartService().getPacketUUID(), new HandlePacketOutRestartService(), PacketOutRestartService.class)
                .registerHandler(new PacketOutSendCommand().getPacketUUID(), new HandlePacketOutSendCommand(), PacketOutSendCommand.class);

        Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("netty-client-connected")
                        .replace("%address%", config.getManagerAddress()).replace("%port%", "" + config.getNetworkingCommunication()));
        }

    @Override
    public void run() {

        cloudServiceDriver = new CloudServiceDriver();
        Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("node-try-start"));
        config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
        System.setProperty("log4j.configurationFile", "log4j2.properties");
        if (!new File("./connection.key").exists()){
            Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("connection-key-not-exists"));
            System.exit(0);
        }
        if (!new File("./local/server-icon.png").exists()){
            Driver.getInstance().getTerminalDriver().log(Type.INFO,Driver.getInstance().getLanguageDriver().getLang().getMessage("try-to-download-server-icon"));
            Driver.getInstance().getMessageStorage().packetLoader.loadLogo();
            new AnimationDriver().play();
        }
        if (!new File("./local/GLOBAL/EVERY/plugins/metacloud-api.jar").exists()){
            Driver.getInstance().getTerminalDriver().log(Type.INFO,Driver.getInstance().getLanguageDriver().getLang().getMessage("try-to-download-cloudapi"));
            Driver.getInstance().getMessageStorage().packetLoader.loadAPI();
            new AnimationDriver().play();
        }
        if (!new File("./local/GLOBAL/EVERY/plugins/metacloud-plugin.jar").exists()){
            Driver.getInstance().getTerminalDriver().log(Type.INFO,Driver.getInstance().getLanguageDriver().getLang().getMessage("try-to-download-plugin"));
            Driver.getInstance().getMessageStorage().packetLoader.loadPlugin();
        }
        initNetty(config);
        Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("try-to-load-commands"));
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new HelpCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new StopCommand());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new UpdateCommand());
        Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("commands-was-loaded")
                .replace("%commands%", "" + Driver.getInstance().getTerminalDriver().getCommandDriver().getCommands().size()));
        Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("cloud-start-successful"));
        AuthenticatorKey authConfig = (AuthenticatorKey) new ConfigDriver("./connection.key").read(AuthenticatorKey.class);
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInAuthNode(config.getNodeName(), Driver.getInstance().getMessageStorage().base64ToUTF8(authConfig.getKey())));

    }
}
