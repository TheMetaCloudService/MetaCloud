package eu.metacloudservice.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.DesignConfig;
import eu.metacloudservice.config.IconBase;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.moduleside.translate.Translator;
import eu.metacloudservice.velocity.listener.CloudEventHandler;
import eu.metacloudservice.velocity.listener.MOTDListener;
import eu.metacloudservice.velocity.listener.TablistListener;
import eu.metacloudservice.webserver.RestDriver;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Plugin(id = "syncproxy", name = "metacloud-syncproxy", version = "1.1.0-RELEASE", authors = "RauchigesEtwas",dependencies = {@Dependency(id = "metacloudapi"), @Dependency(id = "metacloudplugin", optional = true)})
public class VeloCityBootstrap {

    private static VeloCityBootstrap instance;
    private final ProxyServer proxyServer;
    private LiveService liveService;
    private RestDriver restDriver;
    public Integer motdCount;
    public DesignConfig configuration;
    public  Configuration conf;
    public Group group;
    public IconBase iconBase;
    public Integer tabCount;

    public Translator translator;

    public MiniMessage message;

    @Inject
    public VeloCityBootstrap(ProxyServer proxyServer) {
        instance = this;
        this.proxyServer = proxyServer;
    }


    @Subscribe
    public void handel(ProxyInitializeEvent event){
        new Driver();
        tabCount = 0;
        motdCount = 0;

        liveService = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        restDriver = new RestDriver(liveService.getManagerAddress(), liveService.getRestPort());
        proxyServer.getEventManager().register(instance, new MOTDListener());
        proxyServer.getEventManager().register(instance, new TablistListener(proxyServer));
        this.message = MiniMessage.miniMessage();
        this.translator = new Translator();
        CloudAPI.getInstance().getEventDriver().registerListener(new CloudEventHandler());

        updater();
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public LiveService getLiveService() {
        return liveService;
    }

    public RestDriver getRestDriver() {
        return restDriver;
    }

    public Integer getMotdCount() {
        return motdCount;
    }

    public DesignConfig getConfiguration() {
        return configuration;
    }

    public Group getGroup() {
        return group;
    }

    public Integer getTabCount() {
        return tabCount;
    }

    public static VeloCityBootstrap getInstance() {
        return instance;
    }

    private void  updater() {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            iconBase = (IconBase) new ConfigDriver().convert(getRestDriver().get("/module/syncproxy/icons"), IconBase.class);
            if (iconBase == null) return;
            conf = (Configuration) new ConfigDriver().convert(getRestDriver().get("/module/syncproxy/configuration"), Configuration.class);
            if (conf == null) return;
            conf.getConfiguration().stream()
                    .filter(designConfig -> designConfig.getTargetGroup().equalsIgnoreCase(liveService.getGroup()))
                    .findFirst()
                    .ifPresent(config -> {
                        configuration = config;
                        group = CloudAPI.getInstance().getGroupPool().getGroup(getLiveService().getGroup());
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
