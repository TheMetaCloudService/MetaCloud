package eu.metacloudservice.moduleside;

import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.Driver;
import eu.metacloudservice.config.General;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.module.extention.IModule;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.File;

public class MetaModule implements IModule {
    @Override
    public void load() {
        create();
        set();
    }

    @Override
    public void unload() {
        Driver.getInstance().getWebServer().removeRoute("/module/notify/general");
        Driver.getInstance().getWebServer().removeRoute("/module/notify/configuration");
    }

    @Override
    public void reload() {
        create();
        update();

    }

    public void create(){
        if (!new File("./modules/notify/config.json").exists()){
            new File("./modules/notify/").mkdirs();
            try {
                Configuration configuration = new Configuration();
                configuration.setProxiedServiceConnected("§8[§a▷§8] §7proxy: §a%service_name% §7was §aconnected §8| §b%service_node%");
                configuration.setProxiedServicePrepared("§8[§b▷§8] §7proxy: §e%service_name% §7was §bPrepared §8| §b%service_node%");
                configuration.setProxiedServiceLaunch("§8[§b▷§8] §7proxy: §e%service_name% §7was §eLaunched §8| §b%service_node%");
                configuration.setProxiedServiceDiconnected("§8[§c◁§8] §7proxy: §c%service_name% §7was §cdisconnected");

                configuration.setServiceConnected("§8[§a▷§8] §7service: §a%service_name% §7was §aconnected §8| §b%service_node%");
                configuration.setServicePrepared("§8[§b▷§8] §7service: §e%service_name% §7was §bPrepared §8| §b%service_node%");
                configuration.setServiceLaunch("§8[§e▷§8] §7service: §e%service_name% §7was §eLaunched §8| §b%service_node%");
                configuration.setServiceDiconnected("§8[§c◁§8] §7service: §c%service_name% §7was §cdisconnected");

                new ConfigDriver("./modules/notify/config.json").save(configuration);

            }catch (Exception e){
                create();
                set();
            }
        }
    }

    public void set(){


        try {
            Configuration configuration = (Configuration) new ConfigDriver("./modules/notify/config.json").read(Configuration.class);

            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/notify/configuration", new ConfigDriver().convert(configuration)));
        }catch (Exception e){
            create();
            set();
        }
    }

    public void update(){
        try {
            Configuration configuration = (Configuration) new ConfigDriver("./modules/notify/config.json").read(Configuration.class);

            Driver.getInstance().getWebServer().updateRoute("/module/notify/configuration", new ConfigDriver().convert(configuration));
        }catch (Exception e){
            create();
            update();
        }
    }
}

