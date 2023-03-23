package eu.metacloudservice.bungee;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bungee.listener.MotdListener;
import eu.metacloudservice.bungee.listener.TabListListener;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.DesignConfig;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import eu.metacloudservice.webserver.RestDriver;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BungeeBootstrap extends Plugin {


    private static BungeeBootstrap instance;
    private LiveService liveService;
    private RestDriver restDriver;
    public Integer motdCount;
    public DesignConfig configuration;
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
        Configuration conf = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/syncproxy/configuration"), Configuration.class);
        if (conf.getConfiguration().stream().anyMatch(designConfig -> designConfig.getTargetGroup().equalsIgnoreCase(liveService.getGroup()))){
            configuration = conf.getConfiguration().stream().filter(designConfig -> designConfig.getTargetGroup().equalsIgnoreCase(liveService.getGroup())).findFirst().get();
        }
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

    private void  updater() {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            Configuration conf = (Configuration) new ConfigDriver().convert(BungeeBootstrap.getInstance().getRestDriver().get("/module/syncproxy/configuration"), Configuration.class);
            conf.getConfiguration().stream()
                    .filter(designConfig -> designConfig.getTargetGroup().equalsIgnoreCase(liveService.getGroup()))
                    .findFirst()
                    .ifPresent(config -> {
                        configuration = config;
                        group = CloudAPI.getInstance().getGroups().stream()
                                .filter(group1 -> group1.getGroup().equalsIgnoreCase(BungeeBootstrap.getInstance().getLiveService().getGroup()))
                                .findFirst()
                                .orElseThrow(); // or handle the case when no group is found
                        tabCount = tabCount >= configuration.getTablist().size() - 1 ? 0 : tabCount + 1;
                        if (group.isMaintenance()) {
                            motdCount = motdCount >= configuration.getMaintenancen().size() - 1 ? 0 : motdCount + 1;
                        } else {
                            motdCount = motdCount >= configuration.getDefaults().size() - 1 ? 0 : motdCount + 1;
                        }
                    });
        }, 0, 5, TimeUnit.SECONDS);
    }
}
