package eu.metacloudservice.bukkit;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bukkit.listener.ReloadBlocker;
import eu.metacloudservice.bukkit.listener.ServiceConnectListener;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.TimerTask;

public class BukkitBootstrap extends JavaPlugin {

    @Override
    public void onEnable(){
        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        CloudAPI.getInstance().setState(ServiceState.LOBBY, service.getService());
        Bukkit.getPluginManager().registerEvents(new ReloadBlocker(), this);
        Bukkit.getPluginManager().registerEvents(new ServiceConnectListener(), this);
        new TimerBase().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!NettyDriver.getInstance().nettyClient.getChannel().isActive()){
                    System.exit(0);
                }
            }
        }, 10, 10, TimeUtil.SECONDS);
    }
}
