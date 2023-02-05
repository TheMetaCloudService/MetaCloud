package eu.themetacloudservice.bukkit;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.bukkit.events.ReloadBlocker;
import eu.themetacloudservice.bukkit.events.ServiceConnectListener;
import eu.themetacloudservice.bukkit.networking.AuthHandler;
import eu.themetacloudservice.bukkit.networking.ServiceConnectedHandler;
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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class CloudPlugin extends JavaPlugin {

    private static  CloudPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        new Driver();
        new NettyDriver();
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents( new ReloadBlocker(), this);
        pluginManager.registerEvents( new ServiceConnectListener(), this);
        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);

        NettyDriver.getInstance().nettyClient = new NettyClient();

        NettyDriver.getInstance().nettyClient.bind(service.getManagerAddress(), service.getNetworkPort());
        NettyDriver.getInstance().nettyClient.connect();
        NettyDriver.getInstance().packetDriver
                .handelPacket(PackageAuthenticByManager.class)
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

        Bukkit.getOnlinePlayers().forEach((Consumer<Player>) player -> {
            player.kickPlayer("CloudService is shutdown");
        });

        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        PackageServiceShutdown packageServiceShutdown = new PackageServiceShutdown(service.getService());
        NettyDriver.getInstance().nettyClient.sendPacket(packageServiceShutdown);
        NettyDriver.getInstance().nettyClient.close();

    }

    public static CloudPlugin getInstance() {
        return instance;
    }
}
