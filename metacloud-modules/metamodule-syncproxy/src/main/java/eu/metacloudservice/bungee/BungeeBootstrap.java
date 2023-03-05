package eu.metacloudservice.bungee;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bungee.listener.MotdListener;
import eu.metacloudservice.bungee.listener.TabListListener;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import eu.metacloudservice.webserver.RestDriver;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.TimerTask;
import java.util.stream.Collectors;

public class BungeeBootstrap extends Plugin {


    private static BungeeBootstrap instance;
    private LiveService liveService;
    private RestDriver restDriver;
    public Integer motdCount;
    public  Configuration configuration;
    public  Group group;
    public Integer tabCount;

    @Override
    public void onEnable() {
        new Driver();
        tabCount = 0;
        motdCount = 0;
        instance = this;
        liveService = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        restDriver = new RestDriver(liveService.getManagerAddress(), liveService.getRestPort());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new MotdListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new TabListListener());
        updater();

    }

    @Override
    public void onDisable() {

    }


    public RestDriver getRestDriver() {
        return restDriver;
    }

    public static BungeeBootstrap getInstance() {
        return instance;
    }

    public LiveService getLiveService() {
        return liveService;
    }

    private void  updater(){
        new Thread(() -> {
            new TimerBase().schedule(new TimerTask() {
                @Override
                public void run() {
                    configuration = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/modules/syncproxy"), Configuration.class);
                    group = CloudAPI.getInstance().getGroups().stream().filter(group1 -> group1.getGroup().equalsIgnoreCase(BungeeBootstrap.getInstance().getLiveService().getGroup())).findFirst().get();

                }
            }, 0, 2, TimeUtil.SECONDS);
            new TimerBase().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (tabCount >= (long) configuration.getTablist().stream().toList().size()-1){
                        tabCount = 0;
                    }else {
                        tabCount++;
                    }
                    if (group.isMaintenance()){
                        if (motdCount >= (long) configuration.getMaintenancen().stream().toList().size()-1){
                            motdCount = 0;
                        }else {
                            motdCount++;
                        }
                    }else {
                        if (motdCount >= (long) configuration.getDefaults().stream().toList().size()-1){
                            motdCount = 0;
                        }else {
                            motdCount++;
                        }
                    }

                }
            }, 0, 5, TimeUtil.SECONDS);
        }).start();
    }
}
