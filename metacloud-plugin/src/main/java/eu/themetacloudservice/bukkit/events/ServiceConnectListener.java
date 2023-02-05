package eu.themetacloudservice.bukkit.events;

import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.themetacloudservice.network.cloudplayer.PackageCloudPlayerChangeService;
import eu.themetacloudservice.networking.NettyDriver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServiceConnectListener implements Listener {

    @EventHandler
    public void handle(PlayerJoinEvent event){
        LiveService liveService = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        PackageCloudPlayerChangeService service = new PackageCloudPlayerChangeService(event.getPlayer().getName(), liveService.getService());
        NettyDriver.getInstance().nettyClient.sendPacket(service);
    }
}
