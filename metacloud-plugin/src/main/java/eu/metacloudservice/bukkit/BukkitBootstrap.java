package eu.metacloudservice.bukkit;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.bukkit.listener.ReloadBlocker;
import eu.metacloudservice.bukkit.listener.ServiceConnectListener;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.process.ServiceState;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitBootstrap extends JavaPlugin {

    @Override
    public void onEnable(){
        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        CloudAPI.getInstance().setState(ServiceState.LOBBY, service.getService());
        Bukkit.getPluginManager().registerEvents(new ReloadBlocker(), this);
        Bukkit.getPluginManager().registerEvents(new ServiceConnectListener(), this);
    }
}
