package eu.themetacloudservice.bukkit;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.bukkit.listener.CloudPlayerHandler;
import eu.themetacloudservice.bukkit.networking.AuthHandler;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.themetacloudservice.network.autentic.PackageAuthenticByManager;
import eu.themetacloudservice.network.autentic.PackageAuthenticRequestFromManager;
import eu.themetacloudservice.network.autentic.PackageCallBackAuthenticByManager;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.client.NettyClient;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CloudPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new Driver();
        new NettyDriver();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new CloudPlayerHandler(), this);

        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);

        NettyDriver.getInstance().nettyClient = new NettyClient();

        NettyDriver.getInstance().nettyClient.bind(service.getManagerAddress(), service.getNetworkPort());
        NettyDriver.getInstance().packetDriver
                .handelPacket(PackageAuthenticByManager.class)
                .handelPacket(PackageCallBackAuthenticByManager.class)
                .handelPacket(PackageAuthenticRequestFromManager.class)
                .handelListener(new AuthHandler());

    }

    @Override
    public void onDisable() {

    }
}
