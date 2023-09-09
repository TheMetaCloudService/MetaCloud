package eu.metacloudservice.moduleside;

import eu.metacloudservice.config.*;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.module.extention.IModule;
import eu.metacloudservice.moduleside.commands.SyncProxyCommand;
import eu.metacloudservice.moduleside.events.SyncEvents;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MetaModule implements IModule {
    @Override
    public void load() {


        create();
        set();
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new SyncProxyCommand());
        Driver.getInstance().getMessageStorage().eventDriver.registerListener(new SyncEvents());
    }

    @Override
    public void unload() {
        Driver.getInstance().getWebServer().removeRoute("/module/syncproxy/general");
        Driver.getInstance().getWebServer().removeRoute("/module/syncproxy/configuration");
    }

    @Override
    public void reload() {
        create();
        update();
    }


    private static void create(){

            if (!new File("./modules/syncproxy/config.json").exists()) {
                new File("./modules/syncproxy/").mkdirs();
                Configuration configuration = new Configuration();
                ArrayList<DesignConfig> configs = new ArrayList<>();
                Driver.getInstance().getGroupDriver().getAll().stream().filter(group -> group.getGroupType().equalsIgnoreCase("PROXY")).forEach(group -> {
                    DesignConfig config = new DesignConfig();
                    config.setMotdEnabled(true);
                    config.setTabEnabled(true);
                    config.setTargetGroup(group.getGroup());
                    ArrayList<Motd> maintenance = new ArrayList<>();

                    Motd maintenanceLayout02 = new Motd();

                    ArrayList<String> maintenancePlayerInfo02 = new ArrayList<>();
                    maintenanceLayout02.setProtocol("§8▷ §cMaintenance");
                    maintenanceLayout02.setFirstline("§8► §bMetaCloud  §8▷ §7Ready §ffor §7Future | §f1.16-1.20.1");
                    maintenanceLayout02.setSecondline("§8➥ ✎ §7The network now in §fMAINTENANCE §8| §8◣ §b%proxy_name% §8◥ ");

                    maintenancePlayerInfo02.add("");
                    maintenancePlayerInfo02.add("§8► §bMetaCloud §8▷ §7Ready §ffor §7Future");
                    maintenancePlayerInfo02.add("");
                    maintenancePlayerInfo02.add("  Our Discord §8▷ §b4kKEcaP9WC");
                    maintenancePlayerInfo02.add("  powered by §8▷ §bVortexHostDE");
                    maintenancePlayerInfo02.add("");
                    maintenanceLayout02.setPlayerinfos(maintenancePlayerInfo02);

                    maintenance.add(maintenanceLayout02);

                    ArrayList<Motd> defaults = new ArrayList<>();

                    Motd defaultsLayout02 = new Motd();

                    ArrayList<String> defaultsPlayerInfo02 = new ArrayList<>();
                    defaultsLayout02.setProtocol("§8▷ §b%online_players%§8/§b%max_players%");
                    defaultsLayout02.setFirstline("§8► §bMetaCloud §8▷ §7Ready §ffor §7Future | §f1.16-1.20.1");
                    defaultsLayout02.setSecondline("§8➥ ✎ §7The network now §fONLINE §8| §8◣ §b%proxy_name% §8◥");
                    defaultsPlayerInfo02.add("");
                    defaultsPlayerInfo02.add("§8► §bMetaCloud §8▷ §7Ready §ffor §7Future");
                    defaultsPlayerInfo02.add("");
                    defaultsPlayerInfo02.add("  Our Discord §8▷ §b4kKEcaP9WC");
                    defaultsPlayerInfo02.add("  powered by §8▷ §bVortexHostDE");
                    defaultsPlayerInfo02.add("");
                    defaultsLayout02.setPlayerinfos(defaultsPlayerInfo02);

                    defaults.add(defaultsLayout02);

                    ArrayList<Tablist> tablist = new ArrayList<>();

                    Tablist tablayout01 = new Tablist();
                    tablayout01.setHeader("\n      §8► §bMetaCloud §8▷ §7Ready §ffor §7Future §8◄      \n   §8► §7Current server §8▷ §b%service_name% §8◄   \n");

                    tablayout01.setFooter("\n§8► §7Web §8▷ §bmetacloudservice.eu §8◄\n   §8► §7Powered by §8▷ §bVortexHostDE §8◄   \n");

                    tablist.add(tablayout01);

                    Tablist tablayout02 = new Tablist();
                    tablayout02.setHeader("\n      §8► §bMetaCloud §8▷ §7Ready §ffor §7Future §8◄      \n    §8► §7Current players §8▷  §b%online_players% §r/ §b%max_players% §8◄   \n");
                    tablayout02.setFooter("\n§8► §7Web §8▷ §bmetacloudservice.eu §8◄\n   §8► §7Powered by §8▷ §bVortexHostDE §8◄   \n");

                    tablist.add(tablayout02);
                    config.setMaintenancen(maintenance);
                    config.setTablist(tablist);
                    config.setDefaults(defaults);
                    configs.add(config);
                });
                configuration.setConfiguration(configs);


                new ConfigDriver("./modules/syncproxy/config.json").save(configuration);
            }

        new TimerBase().scheduleAsync(new TimerTask() {
            @Override
            public void run() {
                set();
                update();
            }
        }, 5, TimeUtil.SECONDS);


    }
    
    public static void set(){


        General general = new General("syncproxy", "BETA-1.0.7", "RauchigesEtwas");
        if (Driver.getInstance().getWebServer().getRoute("/module/syncproxy/general") == null)
          Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/syncproxy/general", new ConfigDriver().convert(general)));

        Configuration config = (Configuration) new ConfigDriver("./modules/syncproxy/config.json").read(Configuration.class);
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/syncproxy/configuration", new ConfigDriver().convert(config)));
    }

    public static void update(){

        try {

            Configuration config = (Configuration) new ConfigDriver("./modules/syncproxy/config.json").read(Configuration.class);

            Driver.getInstance().getWebServer().updateRoute("/module/syncproxy/configuration", new ConfigDriver().convert(config));
        }catch (Exception e){
            set();
            update();
        }

    }
}
