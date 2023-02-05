package eu.themetacloudservice.bungeecord;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.bungeecord.command.CloudCommand;
import eu.themetacloudservice.bungeecord.command.LobbyCommand;
import eu.themetacloudservice.bungeecord.listeners.CloudConnectListener;
import eu.themetacloudservice.bungeecord.networking.AuthHandler;
import eu.themetacloudservice.bungeecord.networking.ServiceConnectedHandler;
import eu.themetacloudservice.bungeecord.utils.LobbyDriver;
import eu.themetacloudservice.bungeecord.utils.server.ServerDriver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.serviceconfig.LiveService;
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
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceExit;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceLaunch;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelSync;
import eu.themetacloudservice.network.service.*;
import eu.themetacloudservice.network.service.proxyconnect.PackageConnectedProxyCallBack;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.client.NettyClient;
import eu.themetacloudservice.webserver.RestDriver;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class CloudPlugin extends Plugin {

    private static CloudPlugin instance;
    private ServerDriver serverDriver;
    private LobbyDriver lobbyDriver;
    private RestDriver restDriver;
    private Integer currentPlayers;

    @Override
    public void onEnable() {
        instance = this;
        new Driver();
        lobbyDriver = new LobbyDriver();
        new NettyDriver();
        currentPlayers = 0;
        this.serverDriver = new ServerDriver();
       PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.registerListener(this, new CloudConnectListener());
        pluginManager.registerCommand(this, new CloudCommand("cloud"));
        pluginManager.registerCommand(this, new CloudCommand("metacloud"));
        pluginManager.registerCommand(this, new LobbyCommand("hub"));
        pluginManager.registerCommand(this, new LobbyCommand("lobby"));
        pluginManager.registerCommand(this, new LobbyCommand("l"));
        pluginManager.registerCommand(this, new LobbyCommand("leave"));

        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);

        this.restDriver = new RestDriver(service.getManagerAddress(), service.getRestPort());
        NettyDriver.getInstance().nettyClient = new NettyClient();

        NettyDriver.getInstance().nettyClient.bind(service.getManagerAddress(), service.getNetworkPort());
        NettyDriver.getInstance().nettyClient.connect();
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
                .handelListener(new AuthHandler())
                .handelListener(new ServiceConnectedHandler());
    }

    @Override
    public void onDisable() {

        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
            proxiedPlayer.disconnect("CloudService is shutdown");
        });

        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        PackageServiceShutdown packageServiceShutdown = new PackageServiceShutdown(service.getService());
        NettyDriver.getInstance().nettyClient.sendPacket(packageServiceShutdown);
        NettyDriver.getInstance().nettyClient.close();
    }

    public ServerDriver getServerDriver() {
        return serverDriver;
    }

    public static CloudPlugin getInstance() {
        return instance;
    }

    public LobbyDriver getLobbyDriver() {
        return lobbyDriver;
    }

    public Integer getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(Integer currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public RestDriver getRestDriver() {
        return restDriver;
    }

}
