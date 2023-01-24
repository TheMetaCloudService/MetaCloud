package eu.themetacloudservice.bungeecord;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.bungeecord.command.CloudCommand;
import eu.themetacloudservice.bungeecord.listeners.CloudPlayerHandler;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.themetacloudservice.network.autentic.PackageAuthenticByManager;
import eu.themetacloudservice.network.autentic.PackageAuthenticRequestFromManager;
import eu.themetacloudservice.network.autentic.PackageCallBackAuthenticByManager;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceExit;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceLaunch;
import eu.themetacloudservice.network.nodes.from.PackageToManagerHandelNodeShutdown;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelKillNode;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceExit;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelServiceLaunch;
import eu.themetacloudservice.network.nodes.to.PackageToNodeHandelSync;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.client.NettyClient;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class CloudPlugin extends Plugin {

    @Override
    public void onEnable() {
        new Driver();
        new NettyDriver();
        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.registerListener(this, new CloudPlayerHandler());
        pluginManager.registerCommand(this, new CloudCommand("cloud"));
        pluginManager.registerCommand(this, new CloudCommand("meta"));
        pluginManager.registerCommand(this, new CloudCommand("metacloud"));

        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);

        NettyDriver.getInstance().nettyClient = new NettyClient();

        NettyDriver.getInstance().nettyClient.bind(service.getManagerAddress(), service.getNetworkPort());
        NettyDriver.getInstance().packetDriver
                .handelPacket(PackageAuthenticByManager.class)
                .handelPacket(PackageCallBackAuthenticByManager.class)
                .handelPacket(PackageAuthenticRequestFromManager.class);
    }

    @Override
    public void onDisable() {

    }
}
