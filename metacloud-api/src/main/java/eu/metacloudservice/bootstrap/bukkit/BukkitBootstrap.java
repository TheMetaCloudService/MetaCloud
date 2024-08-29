package eu.metacloudservice.bootstrap.bukkit;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.CloudAPIEnvironment;
import eu.metacloudservice.cloudplayer.codec.gamemode.GameMode;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.service.PacketInServiceDisconnect;
import eu.metacloudservice.player.async.entrys.AsyncCloudPlayer;
import eu.metacloudservice.player.entrys.CloudPlayer;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.service.async.entrys.AsyncCloudService;
import eu.metacloudservice.service.entrys.CloudService;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutionException;

public class BukkitBootstrap extends JavaPlugin {

    @Override
    public void onLoad() {
        new CloudAPI();
    }


    @Override
    public void onEnable() {
        final CloudAPIEnvironment environment = new CloudAPIEnvironment();
        environment.handleNettyConnection();
        environment.registerHandlers();
        environment.handelNettyUpdate();
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("cloudservice-shutdown"));
        Thread.sleep(1000);
        final LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInServiceDisconnect(service.getService()));
    }
}
